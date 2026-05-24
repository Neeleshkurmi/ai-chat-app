package com.np.ai.controller;

import com.np.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "q", required = true) String q){
        return chatService.getLLMResponse(q);
    }
}
