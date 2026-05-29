package com.np.ai.dto;

import com.np.ai.entity.ChatMessage;
import com.np.ai.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.lock.qual.NewObject;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private UUID chatId;

    private MessageResponse message;

    //TODO --> Add documents, images, and other response metadata;
}
