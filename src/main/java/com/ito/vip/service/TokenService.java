package com.ito.vip.service;

import com.ito.vip.model.Token;

import java.util.Date;

public interface TokenService {

    Token createToken(String valueField, Long userId, String deviceId, Date loginDate, Date expireDate);

    Token extendToken(Token token);

    Token getTokenByValue(String valueField);

    void deleteTokenByValue(String valueField);

    Boolean isTokenExisted(String token);

}
