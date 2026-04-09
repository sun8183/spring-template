package com.taeyang.spring_template.auth.ui;

import com.taeyang.spring_template.auth.application.AuthService;
import com.taeyang.spring_template.auth.ui.dto.LoginRequest;
import com.taeyang.spring_template.auth.ui.dto.TokenResponse;
import com.taeyang.spring_template.common.response.ApiResponse;
import com.taeyang.spring_template.common.response.enums.SuccessCode;
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
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success(SuccessCode.OK, authService.login(request));
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
