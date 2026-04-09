package com.taeyang.spring_template.member.domain.repository;

import com.taeyang.spring_template.member.domain.entity.RoleEntity;
import com.taeyang.spring_template.member.domain.enums.MemberRoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    // 특정 권한 이름으로 엔티티 조회 (단건)
    Optional<RoleEntity> findByRoleName(MemberRoleType roleName);

    // 여러 권한 이름을 한꺼번에 조회 (로그인/가입 시 유용)
    List<RoleEntity> findAllByRoleNameIn(List<MemberRoleType> roleNames);
}
