package com.kakarote.crm9.erp.sms.util;


import com.kakarote.crm9.common.config.redis.Redis;
import com.kakarote.crm9.common.config.redis.RedisManager;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * redis操作工具类.</br>
 * (基于RedisTemplate)
 * @author tangmanrong
 * 2018年7月19日下午2:56:24
 */
@Data
public class RedisUtils {

    private Redis redis = RedisManager.getRedis();

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        return redis.get(key);
    }

    /**
     * 写入缓存
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            redis.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新缓存
     */
    public boolean getAndSet(final String key, String value) {
        boolean result = false;
        try {
            redis.getSet(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 自增1
     */
    public boolean increment(final String key) {
        boolean result = false;
        try {
            redis.incrBy(key,1);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            redis.del(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置key的生命周期(秒)
     *
     * @param key
     * @param seconds
     */
    public void expireKey(String key, int seconds) {
        redis.expire(key, seconds);
    }
}
