package com.example.freelanceauthservice.repository;


import com.example.freelanceauthservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserId(Long userId);

    Optional<Token> findByToken(String token);
}
