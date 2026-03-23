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
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 조회
        Member member = memberRepository.findById(request.id())
                .orElseThrow(() -> new CommonException(ErrorCode.LOGIN_NOT_FOUND));

        // 2. 도메인 엔티티 내부 로직으로 비밀번호 검증
        member.checkPassword(request.pw(), passwordEncoder);

        // 3. 인증 성공 시 JWT 토큰 생성 및 반환
        String accessToken = jwtTokenProvider.createToken(member.getId(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        return TokenResponse.of(accessToken, refreshToken);
    }
}
