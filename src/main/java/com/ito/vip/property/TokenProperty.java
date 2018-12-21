package com.ito.vip.property;

import org.springframework.beans.factory.annotation.Value;

public class TokenProperty {

    @Value("${token.expiration.days}")
    private Long expiration;

    @Value("${token.cache.local.alive.days}")
    private Long tokenCacheAliveDays;


    public Long getExpiration() {
        return expiration;
    }

    public Long geTokenCacheAliveDays() {
        return tokenCacheAliveDays;
    }

}
