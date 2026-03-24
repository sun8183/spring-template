package com.taeyang.spring_template.auth.service;

import com.taeyang.spring_template.auth.domain.TokenStore;
import com.taeyang.spring_template.auth.provider.JwtTokenProvider;
import com.taeyang.spring_template.common.exception.CommonException;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import com.taeyang.spring_template.member.domain.Member;
import com.taeyang.spring_template.member.dto.LoginRequest;
import com.taeyang.spring_template.member.dto.TokenResponse;
import com.taeyang.spring_template.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenStore tokenStore;

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

        // 4. refresh 저장
        tokenStore.save(member.getIdx(), refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refresh(Long idx, String refreshToken) {

        // 1. 저장된 리프레시 토큰 조회
        String savedToken = tokenStore.findByMemberId(idx)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_REFRESH_TOKEN));

        // 2. 토큰 값 일치 확인
        if (!savedToken.equals(refreshToken)) {
            throw new CommonException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 3. JWT 자체 유효성 검사 (만료, 서명 등)
        jwtTokenProvider.validateToken(refreshToken);

        // 4. 기존 토큰 삭제
        tokenStore.deleteByMemberId(idx);

        // 5. 사용자 조회 (권한 다시 세팅하려고)
        Member member = memberRepository.findById(idx)
                .orElseThrow(() -> new CommonException(ErrorCode.LOGIN_NOT_FOUND));

        // 6. 새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.createToken(member.getId(), member.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        // 7. 새로운 리프레시 토큰 저장
        tokenStore.save(member.getIdx(), newRefreshToken);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

}
