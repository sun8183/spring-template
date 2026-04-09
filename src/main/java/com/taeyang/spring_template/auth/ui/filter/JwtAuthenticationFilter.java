package com.taeyang.spring_template.auth.ui.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taeyang.spring_template.auth.infrastructure.jwt.enums.JwtCode;
import com.taeyang.spring_template.auth.infrastructure.jwt.JwtTokenProvider;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import com.taeyang.spring_template.common.response.ApiResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        JwtCode tokenStatus = jwtTokenProvider.validateToken(token);

        switch (tokenStatus) {
            case VALID:
                // 1. 인증 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // 2. SecurityContext에 저장하여 필터 이후에 검증
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            case EXPIRED:
                writeError(response, ErrorCode.JWT_EXPIRED);
                return;
            case EMPTY:
                break;
            case INVALID:
            default:
                writeError(response, ErrorCode.JWT_INVALID);
                return;
        }

        // 4. 다음 필터로 이동
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 인증에 대한 접근 제어는 SecurityConfig 에서 직접 한다.
        return false;
    }

    // 헤더에서 "Bearer " 접두사를 제거하고 토큰 값만 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 필터에서는 예외가 컨트롤러 까지 전파되지 않아 바로 응답
    private void writeError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus().value());
        String json = objectMapper.writeValueAsString(ApiResponse.error(errorCode));
        response.getWriter().write(json);
    }
}
