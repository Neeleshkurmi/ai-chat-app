package com.np.ai.repository;

import com.np.ai.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, UUID> {
//
//    ChatMessage findAll(UUID chatId);
//
//    ChatMessage findById(UUID chatId, int limit, int offset);
}
