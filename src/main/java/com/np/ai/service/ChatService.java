package com.np.ai.service;

import com.np.ai.dto.ChatRequest;
import com.np.ai.dto.ChatResponse;
import com.np.ai.dto.MessageResponse;
import com.np.ai.dto.NewChatResponse;
import com.np.ai.entity.Chat;
import com.np.ai.entity.ChatMessage;
import com.np.ai.entity.MessageRole;
import com.np.ai.entity.User;
import com.np.ai.repository.ChatRepository;
import com.np.ai.repository.MessageRepository;
import com.np.ai.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final LlmService llmService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    public NewChatResponse createNewChat(ChatRequest chatRequest, User user) {

        log.info("new chat method called");

        log.info("creating new chat");
        Chat chat = new Chat();
        chat.setUser(user);
        chat.setTitle(chatRequest.getQuery().substring(0, Math.min(chatRequest.getQuery().length(), 50)));

        log.info("saving the chat in repo");
        Chat newChat = chatRepository.save(chat);

        log.info("creating chat response");

        ChatResponse chatResponse = getChatResponse(newChat.getId(), chatRequest, user);

        NewChatResponse response = new NewChatResponse();
        response.setTitle(newChat.getTitle());
        response.setChatId(newChat.getId());
        response.setMessage(chatResponse.getMessage());

        return response;
    }

    public ChatResponse getChatResponse(UUID chatId, ChatRequest chatRequest, User user){

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(()-> new RuntimeException("chat not found for id: "+chatId));

        String response = llmService.getLLMResponse(chatRequest.getQuery(), chatId.toString());

        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole(MessageRole.USER);
        userMessage.setContent(chatRequest.getQuery());
        userMessage.setChat(chat);
        messageRepository.save(userMessage);

        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setRole(MessageRole.ASSISTANT);
        assistantMessage.setContent(response);
        assistantMessage.setChat(chat);

        ChatMessage savedMessage = messageRepository.save(assistantMessage);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(assistantMessage.getId());
        messageResponse.setContent(assistantMessage.getContent());

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setChatId(chatId);
        chatResponse.setMessage(messageResponse);

        return chatResponse;
    }

}
