package com.example.freelanceauthservice.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super(String.format("Пользователь с именем '%s' не найден", username));
    }
}
