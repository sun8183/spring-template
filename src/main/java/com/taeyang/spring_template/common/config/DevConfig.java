package com.taeyang.spring_template.common.config;

import com.taeyang.spring_template.auth.enums.Role;
import com.taeyang.spring_template.member.domain.Member;
import com.taeyang.spring_template.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevConfig {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner dataInitializer() {
        return args -> {
            // 1. 테스트용 일반 유저
            Member user = Member.builder()
                    .id("testuser")
                    .password(passwordEncoder.encode("password123!"))
                    .role(Role.ROLE_USER)
                    .build();

            // 2. 테스트용 관리자
            Member admin = Member.builder()
                    .id("admin")
                    .password(passwordEncoder.encode("admin123!"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            Member user1 = Member.builder()
                    .id("testuseR")
                    .password(passwordEncoder.encode("password123!"))
                    .role(Role.ROLE_USER)
                    .build();

            memberRepository.save(user);
            memberRepository.save(user1);
            memberRepository.save(admin);

            System.out.println(">>> Dev 환경: 테스트 계정 생성이 완료되었습니다.");
        };
    }
}

