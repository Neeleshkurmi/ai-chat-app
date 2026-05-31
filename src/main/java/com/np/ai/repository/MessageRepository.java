package com.np.ai.repository;

import com.np.ai.dto.MessageResponse;
import com.np.ai.entity.ChatMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findByChatIdOrderByCreatedAtDesc(UUID chatId, Pageable pageable);

    Optional<List<Message>> findByChatId(UUID chatId);

    void deleteByChatId(UUID uuid);

    List<ChatMessage> findAllByChatIdOrderByCreatedAtAsc(UUID uuid);

    Optional<ChatMessage> findTopByChatIdOrderByCreatedAtDesc(UUID chatId);
}
