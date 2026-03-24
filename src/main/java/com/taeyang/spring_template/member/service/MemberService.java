package com.taeyang.spring_template.member.service;

import com.taeyang.spring_template.auth.enums.Role;
import com.taeyang.spring_template.auth.provider.JwtTokenProvider;
import com.taeyang.spring_template.common.exception.CommonException;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import com.taeyang.spring_template.member.domain.Member;
import com.taeyang.spring_template.member.dto.LoginRequest;
import com.taeyang.spring_template.member.dto.TokenResponse;
import com.taeyang.spring_template.member.repository.MemberRepository;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

}
