package com.community.server.controller;

import com.community.server.body.SignInBody;
import com.community.server.body.SignUpBody;
import com.community.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signing")
    public ResponseEntity<?> signing(@Valid @RequestBody SignInBody signInBody) {
        return authService.auth(signInBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpBody signUpBody) {
        return authService.create(signUpBody);
    }
}
