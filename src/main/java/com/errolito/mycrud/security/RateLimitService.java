package com.errolito.mycrud.security;

import com.errolito.mycrud.cache.RateLimitCacheStore;
import com.errolito.mycrud.security.dto.RateLimit;
import com.errolito.mycrud.security.dto.RateLimitConsumeRequest;
import com.errolito.mycrud.security.dto.RateLimitRequest;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final LettuceBasedProxyManager<String> proxyManager;
    private final RateLimitProperties properties;
    private final RateLimitCacheStore cacheStore;

    public boolean tryConsume(RateLimitConsumeRequest request) {
        Bucket bucket = resolveBucket(request);
        return bucket.tryConsume(1);
    }

    public ConsumptionProbe tryConsumeAndReturnRemaining(RateLimitConsumeRequest request) {
        Bucket bucket = resolveBucket(request);
        return bucket.tryConsumeAndReturnRemaining(1);
    }

    private Bucket resolveBucket(RateLimitConsumeRequest request) {
        return proxyManager
                .builder()
                .build(request.getKey(), () -> config(request.getInstance()));
    }

    private BucketConfiguration config(String url) {
        RateLimit rateLimit = getRateLimitByUrl(url);

        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(rateLimit.getCapacity())
                        .refillIntervally(rateLimit.getCapacity(), Duration.ofSeconds(rateLimit.getRefill()))
                        .build())
                .build();
    }

    public List<RateLimit> getRateLimits() {
        List<RateLimit> rateLimits = cacheStore.getAsList("rate-limit", "list");

        if (rateLimits == null || rateLimits.isEmpty()) {
            rateLimits = properties.getRateLimits();

            cacheStore.save("rate-limit", "list", rateLimits);
        }

        for (RateLimit rateLimit : rateLimits) {
            System.out.println(rateLimit);
        }

        return rateLimits;
    }

    public RateLimit getRateLimitByUrl(String url) {
        return getRateLimits()
                .stream()
                .filter(r -> r.getUrl().equalsIgnoreCase(url))
                .findFirst()
                .orElse(RateLimit.builder()
                        .url(url)
                        .capacity(properties.getCapacity())
                        .refill(properties.getRefill())
                        .build()
                );
    }

    public void reset() {
        cacheStore.clear("rate-limit");
    }

    public RateLimit update(RateLimitRequest request) {
        String url = request.getUrl();

        RateLimit rateLimit = RateLimit
                .builder()
                .url(url)
                .capacity(request.getCapacity())
                .refill(request.getRefill())
                .build();

        List<RateLimit> rateLimits = getRateLimits();

        rateLimits.removeIf(r -> r.getUrl().equalsIgnoreCase(url));

        rateLimits.add(rateLimit);

        cacheStore.remove("rate-limit", "list");
        cacheStore.save("rate-limit", "list", rateLimits);

        return rateLimit;
    }
}