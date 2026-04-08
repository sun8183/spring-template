package com.taeyang.spring_template.auth.ui.filter;
import com.taeyang.spring_template.auth.infrastructure.jwt.enums.JwtCode;
import com.taeyang.spring_template.auth.infrastructure.jwt.JwtTokenProvider;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        JwtCode tokenStatus = jwtTokenProvider.validateToken(token);

        switch (tokenStatus) {
            case VALID:
                break;
            case EXPIRED:
                writeError(response, ErrorCode.JWT_EXPIRED);
                return;
            case EMPTY:
                writeError(response, ErrorCode.JWT_EMPTY);
                return;
            case INVALID:
            default:
                writeError(response, ErrorCode.JWT_INVALID);
                return;
        }

        // 4. 다음 필터로 이동
        filterChain.doFilter(request, response);
    }

    // 헤더에서 "Bearer " 접두사를 제거하고 토큰 값만 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 필터에서는 예외가 컨트롤러 까지 도달하지 않아 바로 응답
    private void writeError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String body = String.format(
                "{\"code\":\"%s\",\"message\":\"%s\"}",
                errorCode.code(),
                errorCode.message()
        );

        response.getWriter().write(body);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 아래 경로들로 들어오는 요청은 필터 로직을 아예 타지 않음
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/h2-console");
    }
}
