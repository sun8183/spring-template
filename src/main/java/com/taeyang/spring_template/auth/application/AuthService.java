package com.taeyang.spring_template.auth.application;

import com.taeyang.spring_template.auth.infrastructure.jwt.enums.JwtCode;
import com.taeyang.spring_template.auth.domain.TokenStore;
import com.taeyang.spring_template.auth.infrastructure.jwt.JwtTokenProvider;
import com.taeyang.spring_template.common.exception.CommonException;
import com.taeyang.spring_template.member.domain.Member;
import com.taeyang.spring_template.auth.ui.dto.LoginRequest;
import com.taeyang.spring_template.auth.ui.dto.TokenResponse;
import com.taeyang.spring_template.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.taeyang.spring_template.common.exception.enums.ErrorCode.*;

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
                .orElseThrow(() -> new CommonException(LOGIN_NOT_FOUND));

        // 2. 도메인 엔티티 내부 로직으로 비밀번호 검증
        member.checkPassword(request.pw(), passwordEncoder);

        // 3. 인증 성공 시 JWT 토큰 생성 및 반환
        String accessToken = jwtTokenProvider.createToken(member.getId(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        // 4. tokenStore에 refresh 저장
        tokenStore.save(member.getId(), refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refresh(String id, String refreshToken) {
        // 1. 저장된 리프레시 토큰 조회
        String savedToken = tokenStore.findByMemberId(id)
                .orElseThrow(() -> new CommonException(RT_EMPTY));

        // 2. 토큰 값 일치 확인
        if (!savedToken.equals(refreshToken)) {
            throw new CommonException(RT_NOT_MATCH);
        }

        // 3. JWT 자체 유효성 검사 (만료, 서명 등)
        JwtCode tokenStatus = jwtTokenProvider.validateToken(refreshToken);
        switch (tokenStatus) {
            case VALID:
                break;
            case EXPIRED:
                throw new CommonException(RT_EXPIRED);
            case EMPTY:
                throw new CommonException(RT_EMPTY);
            case INVALID:
            default:
                throw new CommonException(RT_INVALID);
        }

        // 4. 기존 토큰 삭제
        tokenStore.deleteByMemberId(id);

        // 5. 사용자 조회 (권한 다시 세팅하려고)
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(LOGIN_NOT_FOUND));

        // 6. 새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.createToken(member.getId(), member.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        // 7. 새로운 리프레시 토큰 저장
        tokenStore.save(member.getId(), newRefreshToken);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

}
