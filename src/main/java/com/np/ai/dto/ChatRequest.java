package com.np.ai.dto;

import com.google.genai.types.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotNull(message = "query can not be null")
    private String query;


    //TODO --> Add fields to get documents, pdfs, images and more file data
}
