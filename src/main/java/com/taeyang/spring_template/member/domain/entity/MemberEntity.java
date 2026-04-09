package com.taeyang.spring_template.member.domain.entity;

import com.taeyang.spring_template.member.domain.Member;
import com.taeyang.spring_template.member.domain.enums.MemberRoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, unique = true, length = 16)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    // 다중 권한을 위한 일대다 매핑 (One To Many)
    /**
     * cascade = CascadeType.ALL (영속성 전이) - 부모에게 일어나는 모든 상태 변화를 자식에게도 전달
     * orphanRemoval = true - 연결이 끊어진 객체 제거
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoleEntity> memberRoles = new ArrayList<>();

    @Builder
    public MemberEntity(Long idx, String id, String password) {
        this.idx = idx;
        this.id = id;
        this.password = password;
    }

    /**
     * Entity -> Domain 변환
     */
    public Member toDomain() {
        List<MemberRoleType> roles = this.memberRoles.stream()
                .map(mre -> mre.getRole().getRoleName())
                .collect(Collectors.toList());

        return Member.builder()
                .idx(this.idx)
                .id(this.id)
                .password(this.password)
                .roles(roles)
                .build();
    }

    /**
     * Domain -> Entity 변환 (정적 팩토리 메서드)
     */
    public static MemberEntity from(Member member, List<RoleEntity> roleEntities) {
        MemberEntity entity = MemberEntity.builder()
                .idx(member.idx())
                .id(member.id())
                .password(member.password())
                .build();

        // 도메인에 정의된 권한들을 엔티티에 하나씩 매핑
        if (roleEntities != null) {
            roleEntities.forEach(entity::addRole);
        }

        return entity;
    }

    /**
     * 연관관계 편의 메서드 (권한 추가)
     */
    public void addRole(RoleEntity roleEntity) {
        MemberRoleEntity memberRole = MemberRoleEntity.builder()
                .member(this)
                .role(roleEntity)
                .build();
        this.memberRoles.add(memberRole);
    }
}