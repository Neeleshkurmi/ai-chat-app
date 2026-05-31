package com.np.ai.service;

import com.np.ai.entity.Chat;
import com.np.ai.entity.ChatMessage;
import com.np.ai.entity.MessageRole;
import com.np.ai.repository.ChatRepository;
import com.np.ai.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatMemoryServiceImpl implements ChatMemory {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    public void add(@NotNull String conversationId, List<Message> messages) {

        Chat chat = chatRepository.findById(UUID.fromString(conversationId))
                .orElseThrow();

        List<ChatMessage> entities = messages.stream()
                .map(message -> {

                    ChatMessage entity = new ChatMessage();

                    if(message.getMessageType().equals(MessageType.USER)){
                        entity.setContent(message.getText().substring("USER QUERY: ".length()));
                    } else{
                        entity.setContent(message.getText());
                    }

                    if (message instanceof UserMessage) {
                        entity.setRole(MessageRole.USER);
                    } else {
                        entity.setRole(MessageRole.ASSISTANT);
                    }

                    entity.setChat(chat);

                    return entity;
                })
                .toList();

        messageRepository.saveAll(entities);
    }

    @NotNull
    @Override
    public List<Message> get(@NotNull String conversationId) {

        List<ChatMessage> messages =
                messageRepository.findAllByChatIdOrderByCreatedAtAsc(
                        UUID.fromString(conversationId));

        return messages.stream()
                .map(this::toAiMessage)
                .toList();
    }

    private Message toAiMessage(ChatMessage msg) {

        return switch (msg.getRole()) {
            case USER -> new UserMessage(msg.getContent().substring("USER QUERY: ".length()));
            case ASSISTANT -> new AssistantMessage(msg.getContent());
            case ADMIN -> null;
        };
    }

    @Override
    public void clear(@NotNull String conversationId) {
        messageRepository.deleteByChatId(UUID.fromString(conversationId));
    }
}
