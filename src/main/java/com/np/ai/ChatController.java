package com.np.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chatWithAi(@RequestParam(value = "q", required = true) String q){
        return chatClient.prompt(q).call().content();
    }
}
