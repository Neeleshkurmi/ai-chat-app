package com.np.ai.controller;

import com.np.ai.dto.*;
import com.np.ai.entity.User;
import com.np.ai.service.ChatService;
import com.np.ai.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @PostMapping("/c")
    public ResponseEntity<NewChatResponse> createNewChat(
            @RequestParam(value = "q") String query,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user) throws IOException {
        NewChatResponse response = chatService.createNewChat(query, file ,user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/c/{chatId}")
    public ResponseEntity<ChatResponse> getChatResponse(
            @PathVariable UUID chatId,
            @RequestParam("q") String query,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user) throws IOException {
        return new ResponseEntity<>(chatService.getChatResponse(chatId, file, query, user), HttpStatus.OK);
    }


    @GetMapping("/get-chats")
    public ResponseEntity<Page<PageChat>> getUserChats(
            @RequestParam(value = "p", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "s", required = false, defaultValue = "5") int pageSize,
            @AuthenticationPrincipal User user
    ){
        return new ResponseEntity<>(chatService.getPageableUserChats(pageNumber, pageSize, user), HttpStatus.OK);
    }

    @GetMapping("/get/chat-messages/{chatId}")
    public ResponseEntity<List<MessageResponse>> getChatMessages(
            @RequestParam(value = "p", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "s", required = false, defaultValue = "5") int pageSize,
            @PathVariable UUID chatId,
            @AuthenticationPrincipal User user
    ){
        log.info("received parms: p = "+pageNumber+ ", s = " + pageSize);
        return new ResponseEntity<>(chatService.getChatMessages(pageNumber, pageSize, chatId, user), HttpStatus.OK);
    }
}
