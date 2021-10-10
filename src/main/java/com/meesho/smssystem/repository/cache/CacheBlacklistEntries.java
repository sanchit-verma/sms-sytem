package com.meesho.smssystem.repository.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
public class CacheBlacklistEntries {
    private static final Logger logger = LogManager.getLogger(CacheBlacklistEntries.class);
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${redis.key.ttl}")
    private int cacheTtl;

    @Autowired
    public CacheBlacklistEntries(@Qualifier("blacklistCacheRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isPresentInCache(String phoneNumber) {
        boolean isPresent =  Boolean.TRUE.equals(redisTemplate.hasKey(phoneNumber));
        if(isPresent){
            Long TTL = redisTemplate.getExpire(phoneNumber, TimeUnit.SECONDS);
            logger.info(phoneNumber + " present in cache! TTL in seconds -> : " + TTL);
        }
        return isPresent;
    }

    public void insertToCache(String phoneNumber) {
        if (isPresentInCache(phoneNumber)) {
            return;
        }
        redisTemplate.opsForValue()
                .set(phoneNumber, "", cacheTtl, TimeUnit.SECONDS);
        logger.info(phoneNumber + " Successfully cached!");
    }

    public void deleteCacheEntry(String phoneNumber) {
        boolean hasKey = isPresentInCache(phoneNumber);
        if (hasKey) {
            redisTemplate.delete(phoneNumber);
        }
    }


}
