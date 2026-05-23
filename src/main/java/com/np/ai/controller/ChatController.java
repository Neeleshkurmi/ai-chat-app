package com.np.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChatController {

    private final ChatClient geminiChatClient;

    public ChatController(@Qualifier("geminiChatClient")  ChatClient chatClient){
        this.geminiChatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "q", required = true) String q){
        return geminiChatClient.prompt(q).call().content();
    }
}
