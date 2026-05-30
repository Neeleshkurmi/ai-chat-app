package com.np.ai.dto;

import com.np.ai.entity.MessageRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private UUID id;
    private MessageRole role;
    private String content;
}
