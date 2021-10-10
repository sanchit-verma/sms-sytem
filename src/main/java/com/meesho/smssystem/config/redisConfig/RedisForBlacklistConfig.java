package com.meesho.smssystem.config.redisConfig;

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
public class RedisForBlacklistConfig {
    @Value("${redis.blacklist.host}")
    private String redisHostname;
    @Value("${redis.blacklist.port}")
    private int redisPort;
    @Value("${redis.database.blacklist.index}")
    private int blacklistDatabaseIndex;
    @Value("${redis.timeout.seconds.connection}")
    private int connectionTimeout;

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForBlacklistCache(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHostname,redisPort);
        config.setDatabase(blacklistDatabaseIndex);
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(connectionTimeout))
                .build();
        return new JedisConnectionFactory(config, jedisClientConfiguration);
    }
    @Bean(name = "blacklistCacheRedisTemplate")
    public RedisTemplate<String, String> blacklistCacheRedisTemplate() {
        final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryForBlacklistCache());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
