package com.community.server.controller;

import com.community.server.body.SignInBody;
import com.community.server.body.SignUpBody;
import com.community.server.service.AuthService;
import com.community.server.service.LauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signing")
    public ResponseEntity<?> signing(@Valid @RequestBody SignInBody signInBody) {
        return authService.auth(signInBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpBody signUpBody) {
        return authService.create(signUpBody);
    }
}
