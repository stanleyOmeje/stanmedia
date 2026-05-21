package com.stan.data_storage.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisUtility {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // ─── SAVE ───────────────────────────────────────────────

    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        log.info("Saved to Redis: key={}", key);
    }

    public void save(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
        log.info("Saved to Redis: key={}, ttl={}", key, ttl);
    }

    // ─── GET ────────────────────────────────────────────────

    public Object get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        log.info("Fetched from Redis: key={}, found={}", key, value != null);
        return value;
    }

    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.info("Cache miss: key={}", key);
            return null;
        }
        log.info("Cache hit: key={}", key);
        return type.cast(value);
    }


    public <T> T getWithObjectMapper(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.info("Cache miss: key={}", key);
            return null;
        }
        log.info("Cache hit: key={}", key);
        return objectMapper.convertValue(value, type);  // ← use this instead of type.cast()
    }

    // ─── DELETE ─────────────────────────────────────────────

    public void delete(String key) {
        redisTemplate.delete(key);
        log.info("Deleted from Redis: key={}", key);
    }

    public void deleteAll(List<String> keys) {
        redisTemplate.delete(keys);
        log.info("Deleted {} keys from Redis", keys.size());
    }

    // ─── UTILS ──────────────────────────────────────────────

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setTtl(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }

    public Long getTtl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

}
