package com.taeyang.spring_template.auth.infrastructure;

import com.taeyang.spring_template.auth.domain.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class RedisTokenStore implements TokenStore {

    private final StringRedisTemplate redisTemplate;

    // Refresh Token의 유효기간 (예: 14일)
    private static final long REFRESH_TOKEN_TTL_DAYS = 14;

    // Redis Key의 중복 방지 및 식별을 위한 접두사 (RT: {memberId} 형태)
    private static final String PREFIX = "RT:";

    /**
     * 사용자의 Refresh Token을 Redis에 저장합니다.
     * @param idx 사용자 고유 식별자
     * @param refreshToken 발급된 Refresh Token 문자열
     */
    @Override
    public void save(Long idx, String refreshToken) {
        // opsForValue().set()을 통해 Key-Value 데이터 저장
        // 세 번째 인자로 Duration을 전달하여 Redis 자체의 TTL(자동 만료) 기능을 활용합니다.
        redisTemplate.opsForValue().set(
                PREFIX + idx,
                refreshToken,
                Duration.ofDays(REFRESH_TOKEN_TTL_DAYS)
        );
    }

    /**
     * 사용자 ID를 기반으로 Redis에 저장된 Refresh Token을 조회합니다.
     * @param idx 사용자 고유 식별자
     * @return 존재하지 않을 경우를 대비해 Optional로 감싸서 반환
     */
    @Override
    public Optional<String> findByMemberId(Long idx) {
        String token = redisTemplate.opsForValue().get(PREFIX + idx);
        return Optional.ofNullable(token);
    }

    /**
     * 사용자의 Refresh Token을 Redis에서 삭제합니다. (로그아웃 시 활용)
     * @param idx 삭제할 토큰의 사용자 식별자
     */
    @Override
    public void deleteByMemberId(Long idx) {
        redisTemplate.delete(PREFIX + idx);
    }
}
