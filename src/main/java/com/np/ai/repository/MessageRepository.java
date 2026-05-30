package com.np.ai.repository;

import com.np.ai.dto.MessageResponse;
import com.np.ai.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findByChatIdOrderByCreatedAtDesc(UUID chatId, Pageable pageable);
}
