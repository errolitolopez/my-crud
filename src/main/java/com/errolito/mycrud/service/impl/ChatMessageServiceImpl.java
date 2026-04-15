package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.ChatMessageQuery;
import com.errolito.mycrud.entity.ChatMessage;
import com.errolito.mycrud.repository.ChatMessageRepository;
import com.errolito.mycrud.service.ChatMessageService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class ChatMessageServiceImpl
        extends BaseCrudServiceImpl<Long, ChatMessageQuery, ChatMessage, ChatMessageRepository>
        implements ChatMessageService {

    protected ChatMessageServiceImpl(ChatMessageRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<ChatMessage> buildLikeSpec(ChatMessageQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("roomId", query.getRoomId())
                        .andEqual("sender", query.getSender())
                        .build();
    }

    @Override
    protected Specification<ChatMessage> buildEqualSpec(ChatMessageQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("roomId", query.getRoomId())
                        .andEqual("sender", query.getSender())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Chat message not found");
    }
}
