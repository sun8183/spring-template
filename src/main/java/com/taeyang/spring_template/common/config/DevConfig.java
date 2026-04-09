package com.taeyang.spring_template.common.config;

import com.taeyang.spring_template.member.domain.entity.MemberEntity;
import com.taeyang.spring_template.member.domain.entity.RoleEntity;
import com.taeyang.spring_template.member.domain.enums.MemberRoleType;
import com.taeyang.spring_template.member.domain.Member;
import com.taeyang.spring_template.member.domain.repository.MemberRepository;
import com.taeyang.spring_template.member.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevConfig {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner dataInitializer(
            MemberRepository memberRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. 기초 데이터: RoleEntity가 DB에 없으면 먼저 생성 (매우 중요!)
            if (roleRepository.count() == 0) {
                roleRepository.save(new RoleEntity(MemberRoleType.ROLE_USER, MemberRoleType.ROLE_USER.getDescription()));
                roleRepository.save(new RoleEntity(MemberRoleType.ROLE_ADMIN, MemberRoleType.ROLE_ADMIN.getDescription()));
                System.out.println(">>> 기본 권한(USER, ADMIN) 생성이 완료되었습니다.");
            }

            // 2. 테스트용 데이터 정의
            // 일반 유저
            Member user = Member.builder()
                    .id("testuser")
                    .password(passwordEncoder.encode("password123!"))
                    .roles(List.of(MemberRoleType.ROLE_USER))
                    .build();

            // 관리자
            Member admin = Member.builder()
                    .id("admin")
                    .password(passwordEncoder.encode("admin123!"))
                    .roles(List.of(MemberRoleType.ROLE_ADMIN))
                    .build();

            // 다중 권한 유저
            Member multiRoleUser = Member.builder()
                    .id("superuseR")
                    .password(passwordEncoder.encode("password123!"))
                    .roles(List.of(MemberRoleType.ROLE_USER, MemberRoleType.ROLE_ADMIN))
                    .build();

            // 3. 저장 로직 (반복되는 부분을 리스트로 처리하면 깔끔합니다)
            List.of(user, admin, multiRoleUser).forEach(memberDomain -> {
                // DB에서 해당 도메인이 가진 RoleType들로 RoleEntity를 모두 찾아옴
                List<RoleEntity> roleEntities = roleRepository.findAllByRoleNameIn(memberDomain.roles());

                // 엔티티 변환 (권한 포함)
                MemberEntity entity = MemberEntity.from(memberDomain, roleEntities);

                // 저장
                memberRepository.save(entity);
            });

            System.out.println(">>> Dev 환경: 테스트 계정(testuser, admin, superuseR) 생성이 완료되었습니다.");
        };
    }
}

