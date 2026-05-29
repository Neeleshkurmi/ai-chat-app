package com.np.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "name must not be null")
    private String name;

    @NotNull(message = "email can not be null")
    private String email;

    @NotNull(message = "password can not be null")
    private String password;
}
