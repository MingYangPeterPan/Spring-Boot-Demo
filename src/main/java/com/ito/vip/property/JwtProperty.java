package com.ito.vip.property;

import org.springframework.beans.factory.annotation.Value;

public class JwtProperty {

    @Value("${jwt.expiration.days}")
    private String expiration;

    @Value("${jwt.extension.threshold.days}")
    private String extensionThreshold;
}
