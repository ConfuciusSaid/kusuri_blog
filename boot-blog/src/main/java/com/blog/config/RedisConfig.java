package com.blog.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    /**
     * redis:
     * host: ${REDIS_HOST:124.223.83.41}
     * port: ${REDIS_PORT:2335}
     * password: ${REDIS_PASSWORD:a.2232281}
     */

    @Value("${spring.redis.host}")
    String host;

    @Value("${spring.redis.port}")
    String port;

    @Value("${spring.redis.password}")
    String password;

    @Bean
    public Redisson redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port)
                .setPassword(password)
        ;
        return (Redisson) Redisson.create(config);
    }
}
