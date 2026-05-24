package com.np.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient geminiChatClient;


    public ChatService(@Qualifier("geminiChatClient") ChatClient chatClient){
        this.geminiChatClient = chatClient;
    }

    public String getLLMResponse(String query){

        Message systemMessage = SystemPromptTemplate.builder()
                .template("you are helpful coding assistant. You are expert in coding.")
                .build().createMessage();
        Message userMessage = PromptTemplate.builder()
                .template("What is {techName} ? and tell me about {techExample}")
                .build().createMessage(Map.of(
                        "techName", "spring",
                        "techExample", "spring boot"
                ));

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return geminiChatClient.prompt(prompt).call().content();
    }

}
