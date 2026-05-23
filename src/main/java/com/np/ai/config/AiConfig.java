package com.np.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean("geminiChatClient")
    public ChatClient geminiChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
