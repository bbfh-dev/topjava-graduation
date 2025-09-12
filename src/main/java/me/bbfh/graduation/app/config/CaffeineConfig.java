package me.bbfh.graduation.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("users", "menus", "votes", "restaurants");
        caffeineCacheManager.setCaffeine(getCaffeine());
        return caffeineCacheManager;
    }

    public Caffeine<Object, Object> getCaffeine() {
        return Caffeine.newBuilder()
                // https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/2.2.2/com/github/benmanes/caffeine/cache/Caffeine.html#maximumSize
                // During testing this number seemed like the best middle ground.
                .maximumSize(5_000)
                .expireAfterWrite(60, TimeUnit.MINUTES);
    }
}
