package com.errolito.mycrud.security;

import io.github.bucket4j.distributed.proxy.ClientSideConfig;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Objects;

import static io.github.bucket4j.distributed.ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public StatefulRedisConnection<String, byte[]> redisRateLimitConnection() {
        RedisClient nativeClient = (RedisClient) lettuceConnectionFactory.getNativeClient();
        return Objects.requireNonNull(nativeClient).connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    public LettuceBasedProxyManager<String> proxyManager(
            StatefulRedisConnection<String, byte[]> redisRateLimitConnection) {
        return LettuceBasedProxyManager.builderFor(redisRateLimitConnection)
                .withClientSideConfig(
                        ClientSideConfig.getDefault()
                                .withExpirationAfterWriteStrategy(basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(2))))
                .build();
    }
}