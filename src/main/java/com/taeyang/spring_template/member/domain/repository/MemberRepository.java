package com.taeyang.spring_template.member.domain.repository;

import com.taeyang.spring_template.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// 1. 엔티티(MemberEntity)를 관리한다고 명시
// 2. PK 타입은 @Id가 붙은 필드의 타입(Long)을 명시
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query("select m from MemberEntity m " +
            "join fetch m.memberRoles mr " +
            "join fetch mr.role " +
            "where m.id = :id")
    Optional<MemberEntity> findByIdWithRoles(@Param("id") String id);
}
