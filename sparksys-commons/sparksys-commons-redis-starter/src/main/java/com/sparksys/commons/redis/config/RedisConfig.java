package com.sparksys.commons.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * description: redis缓存配置
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 13:31:56
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    /**
     * redisTemplate设置
     *
     * @param lettuceConnectionFactory redis连接工厂
     * @return RedisTemplate<String, Object>
     */
    @SuppressWarnings("deprecation")
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisLockRegistry redisLockRegistry(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisLockRegistry redisLockRegistry = new RedisLockRegistry(lettuceConnectionFactory, "sparksys");
        // 超时20秒自动释放锁
        redisLockRegistry.expireUnusedOlderThan(20);
        return redisLockRegistry;
    }

    @Bean
    public RedisLockUtil redisLockUtil(RedisLockRegistry redisLockRegistry) {
        RedisLockUtil redisLockUtil = new RedisLockUtil();
        redisLockUtil.setRedisLockRegistry(redisLockRegistry);
        return redisLockUtil;
    }
}
