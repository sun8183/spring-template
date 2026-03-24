package com.taeyang.spring_template.auth.controller;

import com.taeyang.spring_template.auth.service.AuthService;
import com.taeyang.spring_template.common.exception.dto.SuccessResponse;
import com.taeyang.spring_template.member.dto.LoginRequest;
import com.taeyang.spring_template.member.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(SuccessResponse.of(authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh() {
        return null;
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return null;
    }
}
