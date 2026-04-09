package com.taeyang.spring_template.auth.infrastructure.jwt;

import com.taeyang.spring_template.auth.config.JwtProperties;
import com.taeyang.spring_template.auth.infrastructure.jwt.enums.JwtCode;
import com.taeyang.spring_template.member.domain.enums.MemberRoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static com.taeyang.spring_template.auth.infrastructure.jwt.enums.JwtCode.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey key;

    // SecretKey 값 암호화 추출
    @PostConstruct
    protected void init() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰 생성 (권한 정보 포함)
    public String createToken(String userId, List<MemberRoleType> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .subject(userId)
                .claim("role", roles)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .subject(userId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // JWT 토큰 검증 함수
    public JwtCode validateToken(String token) {
        if (!StringUtils.hasText(token)) return EMPTY;

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return VALID;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
            return EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
            return INVALID;
        }
    }

    // 인증객체 생성 함수
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // 1. 토큰에서 값 꺼내기
        assert claims != null;
        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        // 2. 사용자 객체 생성 (Principal)
        //CustomUser user = new CustomUser(userId, role);

        // 3. 권한 생성 (중요: ROLE_ prefix)
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(role));

        // 4. Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(
                null,       // principal
                null,       // credentials (JWT에서는 필요 없음)
                authorities // 권한 목록
        );
    }

    // 토큰에서 Claims 추출
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}