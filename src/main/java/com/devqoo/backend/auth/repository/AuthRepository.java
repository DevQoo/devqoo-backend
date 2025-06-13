package com.devqoo.backend.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class AuthRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:";
    private static final String BLACKLIST_PREFIX = "blacklist:";


    public AuthRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis 키 생성
    private String key(Long userId) {
        return PREFIX + userId;
    }

    // Refresh Token 저장
    public void saveRefreshToken(Long userId, String refreshToken, int refreshExpireTime) {

        redisTemplate.opsForValue().set(
            key(userId), refreshToken, refreshExpireTime, TimeUnit.SECONDS);
    }

    // Refresh Token 삭제
    public void removeRefreshToken(Long userId) {

        redisTemplate.delete(key(userId));
    }

    // BlackList 키 생성
    private String blackListKey(String accessToken) {
        return BLACKLIST_PREFIX + accessToken;
    }

    // BlackList 확인
    public boolean isAccessTokenBlackList(String accessToken) {
        return "BLACKLIST".equals(redisTemplate.opsForValue().get(blackListKey(accessToken)));
    }

    // BlackList 저장
    public void saveBlackList(String accessToken, Long remainingTime) {

        redisTemplate.opsForValue().set(
            blackListKey(accessToken), "BLACKLIST", remainingTime, TimeUnit.SECONDS);
    }
}
