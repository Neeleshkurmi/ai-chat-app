package com.np.ai.controller;

import com.np.ai.dto.ChatRequest;
import com.np.ai.dto.ChatResponse;
import com.np.ai.dto.NewChatResponse;
import com.np.ai.entity.User;
import com.np.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/c")
    public ResponseEntity<NewChatResponse> createNewChat(
            @RequestBody ChatRequest chatRequest,
            @AuthenticationPrincipal User user){
        NewChatResponse response = chatService.createNewChat(chatRequest, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/c/{chatId}")
    public ResponseEntity<ChatResponse> getChatResponse(
            @PathVariable UUID chatId,
            @RequestBody ChatRequest chatRequest,
            @AuthenticationPrincipal User user){
        return new ResponseEntity<>(chatService.getChatResponse(chatId, chatRequest, user), HttpStatus.OK);
    }
}
