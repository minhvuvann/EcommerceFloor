package vn.mellow.ecom.websocket.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 06/12/2023, Thứ Tư
 **/
@Configuration
@EnableCaching
public class CacheConfig {
    @Value("${app.cache.name}")
    private String cacheName;
    @Value("${app.cache.time}")
    private long cacheTime;
    @Bean
    @Primary
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
        caffeineCacheManager.setCacheNames(List.of(cacheName));
        return caffeineCacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTime, TimeUnit.SECONDS) // Tùy chỉnh thời gian hết hạn của cache
                .maximumSize(100); // Tùy chỉnh số lượng mục trong cache
    }
}
