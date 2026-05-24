package com.np.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient geminiChatClient;

    @Value("classapth:/prompts/user-prompt.st")
    private Resource userPrompt;

    @Value("classath:/prompts/system-prompt.st")
    private Resource systemPrompt;


    public ChatService(@Qualifier("geminiChatClient") ChatClient chatClient){
        this.geminiChatClient = chatClient;
    }

    public String getLLMResponse(String query){
        return geminiChatClient
                .prompt(query)
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

    }

}
