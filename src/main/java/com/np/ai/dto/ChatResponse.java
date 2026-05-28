package com.np.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.lock.qual.NewObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String response;

    //TODO --> Add documents, images, and other response metadata;
}
