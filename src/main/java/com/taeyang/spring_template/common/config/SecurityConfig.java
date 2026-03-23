package com.taeyang.spring_template.common.config;

import com.taeyang.spring_template.auth.filter.JwtAuthenticationFilter;
import com.taeyang.spring_template.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (REST API는 세션을 사용하지 않으므로 필요 없음)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Form Login 및 Http Basic 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 3. 세션을 사용하지 않도록 설정 (Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/login").permitAll() // 로그인, 회원가입 등은 누구나 접근 가능
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 전용 API
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )

                // 5. JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 배치하여 토큰 먼저 검사
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
