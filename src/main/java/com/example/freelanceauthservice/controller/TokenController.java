package com.example.freelanceauthservice.controller;

import com.example.freelanceauthservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    public final TokenService tokenService;

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(tokenService.validateToken(token));
    }
}
