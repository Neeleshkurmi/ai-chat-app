package com.np.ai.controller;

import com.np.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query,
                                       @RequestParam(value = "id") String userId){
        return new ResponseEntity<>(chatService.getLLMResponse(query, userId), HttpStatus.OK);
    }

    @GetMapping("/stream-chat")
    public Flux<String> streamChat(@RequestParam(value = "q", required = true) String q){
        return chatService.streamChat(q);
    }

}
