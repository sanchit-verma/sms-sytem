package com.meesho.smssystem.repository.cache;

import com.meesho.smssystem.model.SMSRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
public class CacheSMSEntries {
    private static final Logger logger = LogManager.getLogger(CacheSMSEntries.class);
    private final RedisTemplate<String, SMSRecord> redisTemplate;
    @Value("${redis.key.ttl}")
    private int cacheTtl;
    @Autowired
    public CacheSMSEntries(@Qualifier("SMSRecordsCacheRedisTemplate") RedisTemplate<String, SMSRecord> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public boolean isPresentInCache(Long Id){
        boolean isPresent =  Boolean.TRUE.equals(redisTemplate.hasKey(Id.toString()));
        if(isPresent){
            Long TTL = redisTemplate.getExpire(Id.toString(), TimeUnit.SECONDS);
            logger.info(Id + " present in cache! TTL in seconds -> : " + TTL);
        }
        return isPresent;
    }

    public void insertInCache(SMSRecord smsRecord){
        if(isPresentInCache(smsRecord.getId())){
            return;
        }
        redisTemplate.opsForValue()
                .set(smsRecord.getId().toString(), smsRecord, cacheTtl, TimeUnit.SECONDS);
        logger.info(smsRecord.getId() + " (SMS Entity) Successfully cached!");

    }

    public SMSRecord getFromCache(Long Id){
        // Check once if entry is in cache using above method before calling this method
        return redisTemplate.opsForValue()
                .get(Id.toString());
    }




}
