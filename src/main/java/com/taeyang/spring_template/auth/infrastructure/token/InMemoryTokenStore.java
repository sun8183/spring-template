package com.taeyang.spring_template.auth.infrastructure.token;

import com.taeyang.spring_template.auth.domain.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis를 이용한 Refresh Token 저장소 구현체입니다.
 * * @Primary: 동일한 인터페이스(TokenStore)를 구현한 클래스가 여러 개일 때,
 * 우선적으로 이 빈(Bean)을 주입하도록 설정합니다. (InMemory 구현체보다 우선됨)
 */
// @Primary
@Component
public class InMemoryTokenStore implements TokenStore {

    // 현업 팁: Map의 변수명을 명확히 하고, 동시성 보장을 위해 ConcurrentHashMap 사용은 유지합니다.
    private final Map<String, String> store = new ConcurrentHashMap<>();

    @Override
    public void save(String memberId, String refreshToken) {
        // null 체크를 추가하여 데이터의 무결성을 보장할 수 있습니다.
        if (memberId == null || refreshToken == null) {
            return;
        }
        store.put(memberId, refreshToken);
    }

    @Override
    public Optional<String> findByMemberId(String memberId) {
        // 메서드 명칭을 인터페이스와 일치시킵니다.
        return Optional.ofNullable(store.get(memberId));
    }

    @Override
    public void deleteByMemberId(String memberId) {
        // 삭제 시에도 명확한 명칭을 사용합니다.
        store.remove(memberId);
    }
}