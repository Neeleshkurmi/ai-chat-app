package com.np.ai.service;

import com.np.ai.dto.*;
import com.np.ai.entity.Chat;
import com.np.ai.entity.ChatMessage;
import com.np.ai.entity.MessageRole;
import com.np.ai.entity.User;
import com.np.ai.ingestion.DataIngestion;
import com.np.ai.repository.ChatRepository;
import com.np.ai.repository.MessageRepository;
import com.np.ai.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final LlmService llmService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final DataIngestion dataIngestion;


    public NewChatResponse createNewChat(String chatRequest, MultipartFile file ,User user) throws IOException {

        log.info("new chat method called");

        log.info("creating new chat");
        Chat chat = new Chat();
        chat.setUser(user);
        chat.setTitle(chatRequest.substring(0, Math.min(chatRequest.length(), 50)));

        log.info("saving the chat in repo");
        Chat newChat = chatRepository.save(chat);

        log.info("creating chat response");

        ChatResponse chatResponse = getChatResponse(newChat.getId(), file, chatRequest, user);

        NewChatResponse response = new NewChatResponse();
        response.setTitle(newChat.getTitle());
        response.setChatId(newChat.getId());
        response.setMessage(chatResponse.getMessage());

        return response;
    }


    @Transactional
    public ChatResponse getChatResponse(UUID chatId, MultipartFile file, String chatRequest, User user) throws IOException {

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(()-> new RuntimeException("chat not found for id: "+chatId));

        if(file!=null){
            dataIngestion.ingest(file, chatId);
        }

        log.info("getting response from llm......");
        String response = llmService.getLLMResponse(chatRequest, chatId.toString());

        log.info("successfully get the response from llm......");

//        ChatMessage userMessage = new ChatMessage();
//        userMessage.setRole(MessageRole.USER);
//        userMessage.setContent(chatRequest);
//        userMessage.setChat(chat);
//        messageRepository.save(userMessage);

//        ChatMessage assistantMessage = new ChatMessage();
//        assistantMessage.setRole(MessageRole.ASSISTANT);
//        assistantMessage.setContent(response);
//        assistantMessage.setChat(chat);

        ChatMessage savedMessage = messageRepository.findTopByChatIdOrderByCreatedAtDesc(chatId)
                .orElseThrow(()-> new RuntimeException("chat not found"));

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(savedMessage.getId());
        messageResponse.setRole(messageResponse.getRole());
        messageResponse.setContent(savedMessage.getContent());

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setChatId(chatId);
        chatResponse.setMessage(messageResponse);

        return chatResponse;
    }

    public Page<PageChat> getPageableUserChats(int pageNumber, int pageSize, User user) {
        if(user == null){
            throw new RuntimeException("something wrong with security");
        }
        log.info("finding chats in repo");

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<PageChat> chats = chatRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        log.info("got the paginated chats");
        return chats;
    }

    public List<MessageResponse> getChatMessages(int pageNumber, int pageSize, UUID chatId, User user) {
        return messageService.getChatMessages(pageNumber, pageSize, chatId, user);
    }
}
