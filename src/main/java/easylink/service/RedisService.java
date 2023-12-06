package easylink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Call out the redis managed by Spring
 */
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean acquireLock(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Boolean lockAcquired = ops.setIfAbsent(key, "LOCK", Duration.ofSeconds(10));

        // Optionally, you can set an expiration time to the lock
        // ops.expire(key, LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        return lockAcquired != null && lockAcquired;
    }
    public void releaseLock(String key) {
        // Simply delete the key to release the lock
        redisTemplate.delete(key);
    }
}
