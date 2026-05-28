package com.np.ai.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewChatResponseTemplate {
    @Max(value = 15, message = "title length should be 15")
    private String title;

    private String response;
}
