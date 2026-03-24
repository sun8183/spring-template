package com.taeyang.spring_template.auth.infrastructure;

import com.taeyang.spring_template.auth.domain.TokenStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis를 이용한 Refresh Token 저장소 구현체입니다.
 * * @Primary: 동일한 인터페이스(TokenStore)를 구현한 클래스가 여러 개일 때,
 * 우선적으로 이 빈(Bean)을 주입하도록 설정합니다. (InMemory 구현체보다 우선됨)
 */
@Primary
@Component
public class InMemoryTokenStore implements TokenStore {
    private final Map<Long, String> tokenMap = new ConcurrentHashMap<>();

    @Override
    public void save(Long idx, String refreshToken) {
        tokenMap.put(idx, refreshToken);
    }

    @Override
    public Optional<String> findByMemberId(Long idx) {
        return Optional.ofNullable(tokenMap.get(idx));
    }

    @Override
    public void deleteByMemberId(Long idx) {
        tokenMap.remove(idx);
    }
}
