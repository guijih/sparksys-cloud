package com.sparksys.commons.redis.cache;

import com.sparksys.commons.redis.constant.CacheConstant;
import com.sparksys.commons.redis.props.PowerCacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * description: 缓存构建器
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:28:24
 */
@Component
@Slf4j
public class PowerCacheBuilder {

    private final CacheProviderService localCacheService;

    private final CacheProviderService redisCacheService;

    private static List<CacheProviderService> providerServiceList = new ArrayList<>();

    private static final Lock providerLock = new ReentrantLock();

    private final PowerCacheProperties powerCacheProperties;

    public PowerCacheBuilder(@Qualifier("localCacheProvider") CacheProviderService localCacheService,
                             @Qualifier("redisCacheProvider") CacheProviderService redisCacheService, PowerCacheProperties powerCacheProperties) {
        this.localCacheService = localCacheService;
        this.redisCacheService = redisCacheService;
        this.powerCacheProperties = powerCacheProperties;
    }

    /**
     * 初始化缓存提供者 默认优先级：先本地缓存，后分布式缓存
     *
     * @return CacheProviderService>
     * @Author zhouxinlei
     * @Date 2020-01-28 14:11:52
     */
    private List<CacheProviderService> getCacheProviders() {
        if (providerServiceList.size() > 0) {
            return providerServiceList;
        }
        //线程安全
        try {
            providerLock.tryLock(1, TimeUnit.MILLISECONDS);
            if (providerServiceList.size() > 0) {
                return providerServiceList;
            }
            CacheProviderService cacheProviderService = null;
            //启用本地缓存
            if (powerCacheProperties.isUseLocalCache()) {
                providerServiceList.add(localCacheService);
            }
            //启用Redis缓存
            if (powerCacheProperties.isUseRedisCache()) {
                providerServiceList.add(redisCacheService);
                resetCacheVersion();//设置分布式缓存版本号
            }
            log.info("初始化缓存提供者成功，共有" + providerServiceList.size() + "个");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化缓存提供者发生异常：{}", e.getMessage());
            providerServiceList = new ArrayList<>();
        } finally {
            providerLock.unlock();
        }
        return providerServiceList;
    }

    /**
     * 查询缓存
     *
     * @param key 缓存键 不可为空
     **/
    public <T extends Object> T get(String key) {
        T obj = null;
        //key = generateVerKey(key);//构造带版本的缓存键
        for (CacheProviderService provider : getCacheProviders()) {
            obj = provider.get(key);
            if (obj != null) {
                return obj;
            }
        }
        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     **/
    public <T extends Object> T get(String key, Function<String, T> function) {
        T obj = null;
        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function);
            } else if (function != null && obj != null) {
                //查询并设置其他缓存提供者程序缓存
                provider.get(key, function);
            }
            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }
        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm function函数的调用参数
     **/
    public <T extends Object, M extends Object> T get(String key, Function<M, T> function, M funcParm) {
        T obj = null;
        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function, funcParm);
            } else if (function != null && obj != null) {
                //查询并设置其他缓存提供者程序缓存
                provider.get(key, function, funcParm);
            }
            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }
        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    public <T> T get(String key, Function<String, T> function, long expireTime) {
        T obj = null;
        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function, expireTime);
            } else {
                provider.get(key, function, expireTime);
            }
            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }
        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm   function函数的调用参数
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    public <T, M> T get(String key, Function<M, T> function, M funcParm, long expireTime) {
        T obj = null;
        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function, funcParm, expireTime);
            } else {
                provider.get(key, function, funcParm, expireTime);
            }
            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }

        return obj;
    }

    /**
     * 设置缓存键值  直接向缓存中插入或覆盖值
     *
     * @param key 缓存键 不可为空
     * @param obj 缓存值 不可为空
     **/
    public <T> void set(String key, T obj) {
        //key = generateVerKey(key);//构造带版本的缓存键
        for (CacheProviderService provider : getCacheProviders()) {

            provider.set(key, obj);

        }
    }

    /**
     * 设置缓存键值  直接向缓存中插入或覆盖值
     *
     * @param key        缓存键 不可为空
     * @param obj        缓存值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    public <T> void set(String key, T obj, Long expireTime) {

        //key = generateVerKey(key);//构造带版本的缓存键

        for (CacheProviderService provider : getCacheProviders()) {

            provider.set(key, obj, expireTime);

        }
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键 不可为空
     * @return void
     * @Author zhouxinlei
     * @Date 2020-01-28 14:09:10
     */
    public void remove(String key) {
        //key = generateVerKey(key);//构造带版本的缓存键
        if (StringUtils.isEmpty(key)) {
            return;
        }
        for (CacheProviderService provider : getCacheProviders()) {
            provider.remove(key);
        }
    }

    /**
     * 是否存在缓存
     *
     * @param key 缓存键 不可为空
     * @return boolean
     * @throws
     * @Author zhouxinlei
     * @Date 2020-01-28 14:08:31
     */
    public boolean contains(String key) {
        boolean exists = false;
        //key = generateVerKey(key);//构造带版本的缓存键
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
     * 获取分布式缓存版本号
     *
     * @return String
     * @Author zhouxinlei
     * @Date 2020-01-28 14:08:04
     */
    public String getCacheVersion() {
        String version = "";
        //未启用Redis缓存
        if (!powerCacheProperties.isUseRedisCache()) {
            return version;
        }
        version = redisCacheService.get(CacheConstant.CACHE_VERSION_KEY);
        return version;
    }

    /**
     * 重置分布式缓存版本  如果启用分布式缓存，设置缓存版本
     *
     * @Author zhouxinlei
     * @Date 2020-01-28 14:07:01
     */
    public void resetCacheVersion() {
        String version = "";
        //未启用Redis缓存
        if (!powerCacheProperties.isUseLocalCache()) {
            return;
        }
        //设置缓存版本
        version = String.valueOf(Math.abs(UUID.randomUUID().hashCode()));
        redisCacheService.set(CacheConstant.CACHE_VERSION_KEY, version);
    }

    /**
     * 如果启用分布式缓存，获取缓存版本，重置查询的缓存key，可以实现相对实时的缓存过期控制
     * <p>
     * 如没有启用分布式缓存，缓存key不做修改，直接返回
     *
     * @param key 缓存key值
     * @return String
     * @Author zhouxinlei
     * @Date 2020-01-28 14:06:08
     */
    public String generateVerKey(String key) {
        String result = key;
        if (StringUtils.isEmpty(key)) {
            return result;
        }
        //没有启用分布式缓存，缓存key不做修改，直接返回
        if (powerCacheProperties.isUseRedisCache()) {
            return result;
        }
        String version = redisCacheService.get(CacheConstant.CACHE_VERSION_KEY);
        if (StringUtils.isEmpty(version)) {
            return result;
        }
        result = String.format("%s_%s", result, version);
        return result;
    }
}
