package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.AiChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatRepository extends JpaRepository<AiChatHistory, Integer> {
    List<AiChatHistory> findAllByConversationId(String conversationId);

    void deleteByConversationId(String conversationId);
}