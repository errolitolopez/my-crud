package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.AiChatHistoryResponse;
import com.errolito.mycrud.dto.AiChatRequest;
import com.errolito.mycrud.service.AiChatService;
import com.errolito.mycrud.shared.BaseController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import io.github.uncaughterrol.commons.utils.TokenGenerator;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai/chat")
public class AiChatController extends BaseController {

    private final AiChatService service;

    public AiChatController(AiChatService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> ask(@Valid @RequestBody AiChatRequest request) {
        return success(service.ask(request));
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamAsk(@Valid @RequestBody AiChatRequest request) {
        return service.streamAsk(request);
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<ApiResponse<List<AiChatHistoryResponse>>> history(@PathVariable String conversationId) {
        return success(service.getAiChatHistoryByConversationId(conversationId));
    }

    @DeleteMapping("/history/{conversationId}")
    public ResponseEntity<ApiResponse<Void>> deleteHistory(@PathVariable String conversationId) {
        service.deleteAiChatHistoryByConversationId(conversationId);
        return success();
    }

    @PostMapping("/conversation-id")
    public ResponseEntity<ApiResponse<String>> createConversationId() {
        return success(TokenGenerator.fastAlphanumericToken(32));
    }
}