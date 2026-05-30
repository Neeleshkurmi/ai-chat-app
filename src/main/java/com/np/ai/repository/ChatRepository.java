package com.np.ai.repository;

import com.np.ai.dto.PageChat;
import com.np.ai.entity.Chat;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @NotNull
    @Override
    Optional<Chat> findById(@NotNull UUID chatId);

    Page<PageChat> findByUserIdOrderByCreatedAtDesc(UUID id, Pageable pageable);
}
