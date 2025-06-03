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


    public AuthRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis 키 생성
    private String key(Long userId) {
        return PREFIX + userId;
    }

//    // Refresh Token 조회
//    public String findByRefreshToken(Long userId) {
//        return redisTemplate.opsForValue().get(key(userId));
//    }

    // Refresh Token 저장
    public void saveRefreshToken(Long userId, String refreshToken, int refreshExpireTime) {

        redisTemplate.opsForValue().set(
            key(userId), refreshToken, refreshExpireTime, TimeUnit.SECONDS);
    }

//    // Refresh Token 삭제
//    public void removeRefreshToken(Long userId) {
//
//        redisTemplate.delete(key(userId));
//    }
}
