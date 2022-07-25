package com.example.cloudstorage.exception;

import org.springframework.security.core.AuthenticationException;

public class MyAuthException extends AuthenticationException {
    public MyAuthException(String message) {
        super(message);
    }
}
