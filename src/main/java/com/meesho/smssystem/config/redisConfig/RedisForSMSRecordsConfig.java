package com.meesho.smssystem.config.redisConfig;

import com.meesho.smssystem.model.SMSRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisForSMSRecordsConfig {

    @Value("${redis.blacklist.host}")
    private String redisHostname;
    @Value("${redis.blacklist.port}")
    private int redisPort;
    @Value("${redis.database.sms-records.index}")
    private int cacheSMSRecordsDatabaseIndex;
    @Value("${redis.timeout.seconds.connection}")
    private int connectionTimeout;

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForSMSRecordsCache(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHostname,redisPort);
        config.setDatabase(cacheSMSRecordsDatabaseIndex);
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(connectionTimeout))
                .build();
        return new JedisConnectionFactory(config, jedisClientConfiguration);
    }

    @Bean(name = "SMSRecordsCacheRedisTemplate")
    public RedisTemplate<String, SMSRecord> redisTemplateForSMSRecordsCache() {
        final RedisTemplate<String, SMSRecord> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryForSMSRecordsCache());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
