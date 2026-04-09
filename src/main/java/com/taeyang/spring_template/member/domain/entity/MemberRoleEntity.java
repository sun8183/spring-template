package com.taeyang.spring_template.member.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private MemberEntity member;

    /**
     * FetchType.Lazy : 지연로딩 (가짜객체를 넣어두고 getRole 시 쿼리로 접근)
     * JoinColumn 은 네이밍 규칙 Table + id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Builder
    public MemberRoleEntity(MemberEntity member, RoleEntity role) {
        this.member = member;
        this.role = role;
    }
}
