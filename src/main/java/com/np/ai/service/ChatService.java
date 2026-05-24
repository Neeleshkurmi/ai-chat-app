package com.np.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient geminiChatClient;


    public ChatService(@Qualifier("geminiChatClient") ChatClient chatClient){
        this.geminiChatClient = chatClient;
    }

    public String getLLMResponse(String query){
        return geminiChatClient.prompt(query).call().content();
    }

}
