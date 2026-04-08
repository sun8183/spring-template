package com.taeyang.spring_template.auth.infrastructure.token;

import com.taeyang.spring_template.auth.domain.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;


@Primary
@Repository
@RequiredArgsConstructor
public class RedisTokenStore implements TokenStore {

    private final StringRedisTemplate redisTemplate;

    // Refresh Token의 유효기간 (설정 파일로 관리하는 것이 더 좋지만, 상수로도 충분합니다)
    private static final long REFRESH_TOKEN_TTL_DAYS = 14;

    // Redis Key 관례: 계층 구조 사용 (auth:rt:식별자)
    private static final String KEY_PREFIX = "auth:rt:";

    @Override
    public void save(String memberId, String refreshToken) {
        // null 체크를 추가하여 Redis에 "null" 문자열이 들어가는 것을 방지합니다.
        if (memberId == null || refreshToken == null) {
            return;
        }

        redisTemplate.opsForValue().set(
                generateKey(memberId),
                refreshToken,
                Duration.ofDays(REFRESH_TOKEN_TTL_DAYS)
        );
    }

    @Override
    public Optional<String> findByMemberId(String memberId) {
        String token = redisTemplate.opsForValue().get(generateKey(memberId));
        return Optional.ofNullable(token);
    }

    @Override
    public void deleteByMemberId(String memberId) {
        redisTemplate.delete(generateKey(memberId));
    }

    /**
     * Redis 키 생성 공통 로직
     */
    private String generateKey(String memberId) {
        return KEY_PREFIX + memberId;
    }
}
