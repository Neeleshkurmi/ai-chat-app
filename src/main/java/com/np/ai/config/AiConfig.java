package com.np.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class AiConfig {

    @Bean("geminiChatClient")
    public ChatClient geminiChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultOptions(
                GoogleGenAiChatOptions.builder()
                        .build()
        ).build();
    }

    @Bean("openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel chatModel, ChatMemory chatMemory){
        return ChatClient.builder(chatModel).build();
    }
}
