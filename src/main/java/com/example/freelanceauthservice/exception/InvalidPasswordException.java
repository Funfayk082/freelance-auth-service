package com.example.freelanceauthservice.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("Неверный пароль");
    }
}
