package com.example.currencyconverter.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@EnableCaching
public class AppConfig {

    public static final String RATES_CACHE = "rates";

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(10).toMillis());
        return RestClient.builder().requestFactory(factory).build();
    }

    @Bean
    public CacheManager cacheManager(ExchangeApiProperties props) {
        CaffeineCacheManager manager = new CaffeineCacheManager(RATES_CACHE);
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(props.cacheTtlMinutes()))
                .maximumSize(200));
        return manager;
    }
}
