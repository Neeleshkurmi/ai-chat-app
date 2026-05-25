package com.np.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient geminiChatClient;

    private final ChatClient openAiChatClient;

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


    public ChatService(@Qualifier("geminiChatClient") ChatClient geminichatClient, @Qualifier("ollamaChatClient") ChatClient openAiChatClient){
        this.geminiChatClient = geminichatClient;
        this.openAiChatClient = openAiChatClient;
    }

    public String getLLMResponse(String query){
        return openAiChatClient
                .prompt(query)
                .advisors(
                        new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(abusiveWords)
                )
                .system(system -> system.text(this.systemPrompt))
                .user(user -> user.text(this.userPrompt))
                .call()
                .content();

    }

    public Flux<String> streamChat(String query){
        return openAiChatClient
                .prompt(query)
                .advisors(
                        new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(abusiveWords)
                )
                .system(system -> system.text(this.systemPrompt))
                .user(user -> user.text(this.userPrompt))
                .stream()
                .content();
    }


}
