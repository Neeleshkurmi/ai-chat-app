package com.np.ai.controller;

import com.np.ai.dto.AuthResponse;
import com.np.ai.dto.LoginRequest;
import com.np.ai.dto.RegisterRequest;
import com.np.ai.dto.RegisterResponse;
import com.np.ai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userService.userLogin(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> login(@RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.OK);
    }

}
