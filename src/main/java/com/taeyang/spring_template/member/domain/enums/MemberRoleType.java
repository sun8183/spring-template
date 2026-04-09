package com.taeyang.spring_template.member.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRoleType {

    ROLE_USER("일반 사용자"),
    ROLE_ADMIN("시스템 관리자");

    private final String description;
}
