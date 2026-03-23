package com.taeyang.spring_template.member.domain;

import com.taeyang.spring_template.auth.enums.Role;
import com.taeyang.spring_template.common.exception.CommonException;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, unique = true, length = 16)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    // 빌더 패턴으로 객체 생성하기
    @Builder
    public Member(String id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    // 도메인에 비밀번호 체크 로직
    public void checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new CommonException(ErrorCode.LOGIN_NOT_MATCH);
        }
    }
}
