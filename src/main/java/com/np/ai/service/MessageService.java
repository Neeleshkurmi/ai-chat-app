package com.np.ai.service;

import com.np.ai.entity.ChatMessage;
import com.np.ai.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    final MessageRepository messageRepository;

    public ChatMessage getAllChatMessages(UUID chatId){
        return messageRepository.findAll(chatId);
    }

    public ChatMessage getChatMessages(UUID chatId, int limit, int offset){
        return messageRepository.findById(chatId, limit, offset);
    }

//    public ChatMessage addNewChatMessage(UUID chatI)
}
