package com.example.freelanceauthservice.service;

import com.example.freelanceauthservice.dto.JsonMessageDto;
import com.example.freelanceauthservice.dto.UserDto;
import com.example.freelanceauthservice.exception.InvalidPasswordException;
import com.example.freelanceauthservice.exception.UserNotFoundException;
import com.example.freelanceauthservice.model.User;
import com.example.freelanceauthservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String login(UserDto userDto) {
        var user = repository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDto.getUsername()));
        if (user.getPassword().hashCode() != userDto.getPassword().hashCode()) {
            var generatedToken = tokenService.getOrGenerateToken(user);
            kafkaTemplate.send("topic1", String.valueOf(new JsonMessageDto(user.getUsername(), generatedToken)));
            return generatedToken;
        } else throw new InvalidPasswordException();
    }

    public String register(UserDto userDto) throws NoSuchAlgorithmException {
        var user = repository.save(new User(userDto.getUsername(), encodePassword(userDto.getPassword())));
        return tokenService.getOrGenerateToken(user);
    }

    private String encodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("md5");
        md.update(password.getBytes(StandardCharsets.UTF_8));
        return Arrays.toString(md.digest());
    }

}
