package com.np.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
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

    private final VectorStore vectorStore;

    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    private final ChatClient openAiChatClient;

    private final ChatMemory chatMemory;

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


    public ChatService(
            @Qualifier("geminiChatClient") ChatClient geminichatClient,
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            ChatMemory chatMemory, VectorStore vectorStore){
        this.vectorStore = vectorStore;
        this.chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        this.geminiChatClient = geminichatClient;
        this.openAiChatClient = openAiChatClient;
        this.chatMemory = chatMemory;
    }



    public String getLLMResponse(String query, String userId){

        SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)
                .similarityThreshold(0.6)
                .query(query)
                .build();

        MessageChatMemoryAdvisor memoryAdvisor =
                MessageChatMemoryAdvisor.builder(chatMemory)
                        .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        List<String> documentList = documents.stream().map(Document::getText).toList();
        String contextData = String.join(",", documentList);

        return openAiChatClient
                .prompt()
                .advisors(
                        memoryAdvisor,
                        new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(abusiveWords)
                )
                .advisors(a -> a
                        .param(ChatMemory.CONVERSATION_ID, userId))
                .system(system -> system
                        .text(this.systemPrompt)
                        .param("context", contextData))
                .user(user -> user
                        .text(userPrompt)
                        .param("query", query))
                .call()
                .content();

    }

    public Flux<String> streamChat(String query){
        return openAiChatClient
                .prompt()
                .advisors(
                        new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(abusiveWords)
                )
                .system(system -> system.text(this.systemPrompt))
                .user(query)
                .stream()
                .content();
    }


}
