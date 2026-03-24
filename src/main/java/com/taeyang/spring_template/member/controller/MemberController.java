package com.taeyang.spring_template.member.controller;

import com.taeyang.spring_template.common.exception.dto.SuccessResponse;
import com.taeyang.spring_template.member.dto.LoginRequest;
import com.taeyang.spring_template.member.dto.TokenResponse;
import com.taeyang.spring_template.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

}
