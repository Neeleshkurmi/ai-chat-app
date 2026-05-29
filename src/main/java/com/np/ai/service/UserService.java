package com.np.ai.service;

import com.np.ai.config.JwtService;
import com.np.ai.dto.*;
import com.np.ai.entity.User;
import com.np.ai.entity.UserRole;
import com.np.ai.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse userLogin(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("user not find for provided email"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("wrong password");
        }

        String jwtToken = jwtService.generateToken(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtToken);
        response.setUserResponse(userResponse);

        return response;
    }


    @Transactional
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        boolean isPresent = userRepository.findByEmail(registerRequest.getEmail()).isPresent();

        if(isPresent){
            throw  new RuntimeException("Email is already registered with another account");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setUserRole(UserRole.USER);
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setName(savedUser.getName());
        userResponse.setEmail(savedUser.getEmail());

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtToken);
        response.setUserResponse(userResponse);

        return response;
    }
}
