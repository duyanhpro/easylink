package easylink;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.cache.redis.ttl}")
    Integer redisTTL = 3600;  // seconds

    @Bean
    public RedisCacheConfiguration getRedisConfig() {
        return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader()) // need this to able to deserialize entity
                .entryTtl(Duration.ofSeconds(redisTTL));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Customize further if needed
        return template;
    }
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        return RedisCacheManager.RedisCacheManagerBuilder
//                .fromConnectionFactory(connectionFactory)
//                .withCacheConfiguration("myCache",
//                        RedisCacheConfiguration.defaultCacheConfig()
//                                .entryTtl(Duration.ofMinutes(10)) // TTL set to 10 minutes
//                )
//                .build();
//    }
//
//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return (builder) -> builder
//                .withCacheConfiguration("customCache",
//                        RedisCacheConfiguration.defaultCacheConfig()
//                                .entryTtl(Duration.ofMinutes(5)) // TTL set to 5 minutes
//                );
//    }
}
