package com.sparksys.commons.redis.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * description: redis缓存实现
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:28:55
 */
@Component("redisCacheProvider")
public class RedisCacheProviderImpl implements CacheProviderService {
    private final RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    private final static long CACHE_MINUTE = 60;

    public RedisCacheProviderImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void initRedisOperation() {
        valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public <T> T get(String key) {
        return get(key, null, null, CACHE_MINUTE);
    }

    @Override
    public <T> T get(String key, Function<String, T> function) {
        return get(key, function, key, CACHE_MINUTE);
    }

    @Override
    public <T, M> T get(String key, Function<M, T> function, M funcParam) {
        return get(key, function, funcParam, CACHE_MINUTE);
    }

    @Override
    public <T> T get(String key, Function<String, T> function, Long expireTime) {
        return get(key, function, key, expireTime);
    }

    @Override
    public <T, M> T get(String key, Function<M, T> function, M funcParm, Long expireTime) {
        T obj = null;
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        expireTime = getExpireTime(expireTime);
        try {
            obj = (T) valueOperations.get(key);
            if (function != null && obj == null) {
                obj = function.apply(funcParm);
                if (obj != null) {
                    //设置缓存信息
                    set(key, obj, expireTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public <T> void set(String key, T obj) {
        set(key, obj, CACHE_MINUTE);
    }

    @Override
    public <T> void set(String key, T obj, Long expireTime) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        if (obj == null) {
            return;
        }
        expireTime = getExpireTime(expireTime);
        valueOperations.set(key, obj);
        expire(key, expireTime);
    }

    @Override
    public void expire(String key, Long expireTime) {
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Long increment(String key, long delta) {
        return valueOperations.increment(key, delta);
    }

    @Override
    public Long decrement(String key, long delta) {
        return valueOperations.decrement(key, delta);
    }

    @Override
    public void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        redisTemplate.delete(key);
    }

    @Override
    public boolean contains(String key) {
        boolean exists = false;
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }
        return exists;
    }

    /**
     * 获取过期时间 单位：毫秒
     *
     * @param expireTime 传人的过期时间 单位毫秒 如小于1分钟，默认为10分钟
     **/
    private Long getExpireTime(Long expireTime) {
        Long result = expireTime;
        if (expireTime == null || expireTime < CACHE_MINUTE) {
            result = CACHE_MINUTE;
        }
        return result;
    }
}
