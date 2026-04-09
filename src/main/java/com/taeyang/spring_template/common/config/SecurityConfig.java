package com.taeyang.spring_template.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taeyang.spring_template.auth.ui.filter.JwtAuthenticationFilter;
import com.taeyang.spring_template.auth.infrastructure.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (세션 + 쿠키 기반 공격 방어용으로 사용하지 않는 옵션)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Form Login 및 Http Basic 인증 비활성화 (기본 로그인 방지)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 3. 세션을 사용하지 않도록 설정 (Stateless - 매 요청마다 JWT로 인증)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/h2-console/**").permitAll() // 로그인, 회원가입 등은 누구나 접근 가능
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 전용 API
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )

                // 5. JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 배치하여 토큰 먼저 검사
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)

                // 6. 동일 출처에 IFRAME 혀용
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, objectMapper);
    }
}
