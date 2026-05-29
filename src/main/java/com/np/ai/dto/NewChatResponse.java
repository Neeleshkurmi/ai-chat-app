package com.np.ai.dto;

import com.np.ai.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewChatResponse{
    private UUID chatId;
    private String title;
    private MessageResponse message;
}