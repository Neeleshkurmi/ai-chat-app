package com.np.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final ChatClient geminiChatClient;

    @Value("classpath:/prompts/user-prompt.st")
    private Resource userPrompt;

    @Value("classpath:/prompts/system-prompt.st")
    private Resource systemPrompt;

    @Value("classpath:/prompts/abusive-words.st")
    private Resource abusiveWordsResource;

    private List<String> abusiveWords;

    @PostConstruct
    public void init() throws Exception{
        abusiveWords = Files.readAllLines(
                abusiveWordsResource.getFile().toPath()
        );
    }


    public ChatService(@Qualifier("geminiChatClient") ChatClient chatClient){
        this.geminiChatClient = chatClient;
    }

    public String getLLMResponse(String query){
        return geminiChatClient
                .prompt(query)
                .advisors(new SimpleLoggerAdvisor(), new SafeGuardAdvisor(abusiveWords))
                .system(this.systemPrompt)
                .user(this.userPrompt)
                .call()
                .content();

    }

}
