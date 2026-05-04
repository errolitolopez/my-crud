package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.AiChatHistoryResponse;
import com.errolito.mycrud.dto.AiChatRequest;
import com.errolito.mycrud.mapper.AiChatHistoryMapper;
import com.errolito.mycrud.repository.AiChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import javax.sql.DataSource;
import java.util.List;

@Service
public class AiChatService {
    private final ChatClient client;
    private final AiChatHistoryMapper mapper;
    private final AiChatRepository repository;

    public AiChatService(
            AiChatRepository repository,
            ChatClient.Builder builder,
            DataSource dataSource, AiChatHistoryMapper mapper
    ) {
        JdbcChatMemoryRepository memoryRepository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(new JdbcTemplate(dataSource))
                .dialect(JdbcChatMemoryRepositoryDialect.from(dataSource))
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(memoryRepository)
                .maxMessages(20)
                .build();

        this.client = builder
                .defaultSystem("You are a general knowledge assistant. Answer questions accurately and clearly. Be extremely concise and avoid unnecessary detail.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        this.mapper = mapper;
        this.repository = repository;
    }

    public String ask(AiChatRequest request) {
        return getAdvisors(request)
                .call()
                .content();
    }

    public Flux<String> streamAsk(AiChatRequest request) {
        return getAdvisors(request)
                .stream()
                .content();
    }

    @Transactional(readOnly = true)
    public List<AiChatHistoryResponse> getAiChatHistoryByConversationId(String conversationId) {
        return repository.findAllByConversationId(conversationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteAiChatHistoryByConversationId(String conversationId) {
        repository.deleteByConversationId(conversationId);
    }

    private ChatClient.ChatClientRequestSpec getAdvisors(AiChatRequest request) {
        return client.prompt()
                .user(request.getMessage())
                .options(OllamaChatOptions.builder().model(request.getModel()).build())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.getConversationId()));
    }
}