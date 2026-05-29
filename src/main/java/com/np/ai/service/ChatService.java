package com.np.ai.service;

import com.np.ai.dto.ChatRequest;
import com.np.ai.dto.ChatResponse;
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
        chat.setTitle(chatRequest.getQuery().substring(0, Math.min(chatRequest.getQuery().length(), 15)));

        log.info("saving the chat in repo");
        Chat newChat = chatRepository.save(chat);

        ChatResponse chatResponse = getChatResponse(newChat.getId(), chatRequest, user);

        log.info("creating chat response");
        NewChatResponse response = new NewChatResponse();
        response.setChatId(newChat.getId());
        response.setTitle(newChat.getTitle());
        response.setContent(chatResponse.getContent());

        return response;
    }

    public ChatResponse getChatResponse(UUID chatId, ChatRequest chatRequest, User user){

        String response = llmService.getLLMResponse(chatRequest.getQuery(), chatId.toString());

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new RuntimeException("chat not found"));

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

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setContent(savedMessage);

        return chatResponse;
    }

}
