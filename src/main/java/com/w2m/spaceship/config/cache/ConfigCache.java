package com.w2m.spaceship.config.cache;


import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cache.spaceships")
@EnableCaching
public class ConfigCache {

    private long expireAfterWrite;
    private int initialCapacity;
    private int maximumSize;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats();
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("spaceships", "spaceshipsByName");
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
