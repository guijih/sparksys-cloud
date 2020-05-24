package com.sparksys.commons.redis.config;

import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * description: redis分布式锁
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 13:32:22
 */
public class RedisLockUtil {

    private static RedisLockRegistry redisLockRegistry;

    public RedisLockUtil() {
    }

    public void setRedisLockRegistry(RedisLockRegistry redisLockRegistry) {
        RedisLockUtil.redisLockRegistry = redisLockRegistry;
    }


    public static Lock lock(String lockKey) {
        return redisLockRegistry.obtain(lockKey);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey  key值
     * @param waitTime 最多等待时间
     * @return boolean
     */
    public static boolean tryLock(String lockKey, int waitTime, TimeUnit unit) {
        Lock lock = lock(lockKey);
        try {
            return lock.tryLock(waitTime, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey  key值
     * @param waitTime 最多等待时间
     * @return boolean
     */
    public static boolean tryLock(String lockKey, int waitTime) {

        Lock lock = lock(lockKey);
        try {
            return lock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey key值
     * @return void
     * @Author zhouxinlei
     * @Date 2020-01-18 16:18:23
     */
    public static void unlock(String lockKey) {
        Lock lock = lock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     *
     * @param lock Lock锁
     * @return void
     * @Author zhouxinlei
     * @Date 2020-01-18 16:18:23
     */
    public static void unlock(Lock lock) {
        lock.unlock();
    }

}
