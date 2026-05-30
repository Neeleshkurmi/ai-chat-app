package com.np.ai.service;

import com.np.ai.dto.MessageResponse;
import com.np.ai.entity.ChatMessage;
import com.np.ai.entity.User;
import com.np.ai.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageResponse> getChatMessages(int pageNumber, int pageSize, UUID chatId, User user) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        Page<ChatMessage> messagePage = messageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);

        List<MessageResponse> messages = new ArrayList<>();

        for(ChatMessage chatMessage : messagePage){
            MessageResponse messageResponse = new MessageResponse(
                    chatMessage.getId(), chatMessage.getRole(), chatMessage.getContent()
            );
            messages.add(messageResponse);
        }
        return messages;
    }
}
