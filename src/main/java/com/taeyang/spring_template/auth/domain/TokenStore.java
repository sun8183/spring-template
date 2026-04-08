package com.taeyang.spring_template.auth.domain;

import java.util.Optional;

public interface TokenStore {

    /**
     * 리프레시 토큰 저장
     * @param memberId 사용자 식별자 (Key)
     * @param refreshToken 발급된 리프레시 토큰 (Value)
     */
    void save(String memberId, String refreshToken);

    /**
     * 사용자 식별자로 저장된 리프레시 토큰 조회
     * @param memberId 사용자 식별자
     * @return 저장된 토큰 (없을 수 있음)
     */
    Optional<String> findByMemberId(String memberId);

    /**
     * 사용자 식별자로 저장된 리프레시 토큰 삭제 (로그아웃 등)
     * @param memberId 사용자 식별자
     */
    void deleteByMemberId(String memberId);
}