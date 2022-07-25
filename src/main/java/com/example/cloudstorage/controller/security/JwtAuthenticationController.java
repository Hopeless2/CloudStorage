package com.example.cloudstorage.controller.security;

import com.example.cloudstorage.model.ExceptionRequest;
import com.example.cloudstorage.model.Login;
import com.example.cloudstorage.model.Token;
import com.example.cloudstorage.service.security.JwtAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class JwtAuthenticationController {
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    private final JwtAuthService jwtAuthService;

    @Autowired
    public JwtAuthenticationController(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    @PostMapping(value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public Token login(@Valid @RequestBody Login authorization) {
        String authToken = jwtAuthService.login(authorization);
        return new Token(authToken);
    }

    @PostMapping(value = LOGOUT)
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        jwtAuthService.logout(authToken);
        return HttpStatus.OK;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ExceptionRequest handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage());
        return new ExceptionRequest(e.getMessage(), 400);
    }
}
