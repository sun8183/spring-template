package com.taeyang.spring_template.auth.filter;
import com.taeyang.spring_template.auth.enums.JwtCode;
import com.taeyang.spring_template.auth.provider.JwtTokenProvider;
import com.taeyang.spring_template.common.exception.CommonException;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.taeyang.spring_template.common.exception.enums.ErrorCode.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {

        String token = resolveToken(request);
        if (token != null) {
            JwtCode tokenStatus = jwtTokenProvider.validateToken(token);
            switch (tokenStatus) {
                case VALID:
                    break;
                case EXPIRED:
                    throw new CommonException(ErrorCode.JWT_EXPIRED);
                case MALFORMED:
                    throw new CommonException(ErrorCode.JWT_MALFORMED);
                case UNSUPPORTED:
                    throw new CommonException(JWT_UNSUPPORTED);
                case SIGNATURE_INVALID:
                    throw new CommonException(ErrorCode.JWT_SIGNATURE_INVALID);
                case EMPTY:
                    throw new CommonException(ErrorCode.JWT_EMPTY);
                case INVALID:
                default:
                    throw new CommonException(JWT_INVALID);
            }
        } else {
            throw new CommonException(ErrorCode.JWT_EMPTY);
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 아래 경로들로 들어오는 요청은 필터 로직을 아예 타지 않음
        return path.startsWith("/api/auth/") ||
                path.startsWith("/h2-console");
    }
}
