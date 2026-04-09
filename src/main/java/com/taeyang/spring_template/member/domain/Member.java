package com.taeyang.spring_template.member.domain;

import com.taeyang.spring_template.member.domain.enums.MemberRoleType;
import com.taeyang.spring_template.common.exception.CommonException;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public record Member(Long idx, String id, String password, List<MemberRoleType> roles) {
    @Builder
    public Member(Long idx, String id, String password, List<MemberRoleType> roles) {
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.roles = roles != null ? roles : new ArrayList<>();
    }

    // 비즈니스 로직: 비밀번호 체크
    public void checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new CommonException(ErrorCode.LOGIN_NOT_MATCH);
        }
    }

    // 비즈니스 로직: 특정 권한을 가지고 있는지 확인
    public boolean hasRole(MemberRoleType targetRole) {
        return roles.contains(targetRole);
    }

    // 비즈니스 로직: 관리자 여부 확인 (리스트 중 하나라도 ADMIN이 있는지)
    public boolean isAdmin() {
        return roles.contains(MemberRoleType.ROLE_ADMIN);
    }
}
