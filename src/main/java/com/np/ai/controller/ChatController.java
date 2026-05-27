package com.np.ai.controller;

import com.np.ai.dto.NewChatResponse;
import com.np.ai.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @GetMapping("c/")
    public ResponseEntity<NewChatResponse> chat(@RequestParam(value = "q") String query,
                                       @RequestParam(value = "id") UUID chatId){
        NewChatResponse response = new NewChatResponse(chatId, chatService.getLLMResponse(query, chatId.toString()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stream-chat")
    public Flux<String> streamChat(@RequestParam(value = "q", required = true) String q){
        return chatService.streamChat(q);
    }

    @GetMapping("c/new-chat")
    public ResponseEntity<NewChatResponse> createNewChat(@RequestParam(value = "q") String query){
        UUID chatId = UUID.randomUUID();
        NewChatResponse response = new NewChatResponse(chatId, chatService.getLLMResponse(query, chatId.toString()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
