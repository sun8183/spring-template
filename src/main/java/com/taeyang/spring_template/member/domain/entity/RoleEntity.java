package com.taeyang.spring_template.member.domain.entity;

import com.taeyang.spring_template.member.domain.enums.MemberRoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private MemberRoleType roleName;

    private String description;

    @Builder
    public RoleEntity(MemberRoleType roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }
}
