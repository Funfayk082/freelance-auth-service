package com.example.freelanceauthservice.service;

import com.example.freelanceauthservice.exception.TokenValidationException;
import com.example.freelanceauthservice.model.Token;
import com.example.freelanceauthservice.model.User;
import com.example.freelanceauthservice.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    public String validateToken(String token) {
        var tokenOpt = tokenRepository.findByToken(token);
        if(tokenOpt.isPresent()) {
            if(tokenOpt.get().getExpirationDate().after(new Date())) {
                return tokenOpt.get().getUserId().toString();
            } else throw new TokenValidationException("Срок действия токена истёк.");
        } else throw new TokenValidationException("Токен не существует");
    }

    public String getOrGenerateToken(User user) {
        var token = tokenRepository.findByUserId(user.getId());
        if(token.isPresent() && token.get().getExpirationDate().after(new Date())) {
            return tokenRepository.findByUserId(user.getId()).orElseThrow().getToken();
        } else {
            String newToken = UUID.randomUUID().toString();
            tokenRepository.save(new Token(user.getId(), newToken, new Date(new Date().getTime() + 1209600 * 1000)));
            return newToken; //TODO сделать генерацию по юзеру
        }
    }
}
