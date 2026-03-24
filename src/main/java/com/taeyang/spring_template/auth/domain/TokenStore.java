package com.taeyang.spring_template.auth.domain;

import java.util.Optional;

public interface TokenStore {

    void save(Long idx, String refreshToken);

    Optional<String> findByMemberId(Long idx);

    void deleteByMemberId(Long idx);
}