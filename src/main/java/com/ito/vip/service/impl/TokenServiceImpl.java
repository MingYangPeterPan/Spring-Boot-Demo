package com.ito.vip.service.impl;

import com.ito.vip.common.annotation.CommonTransactional;
import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.DateFormatUtil;
import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.dao.TokenDAO;
import com.ito.vip.model.Token;
import com.ito.vip.property.TokenProperty;
import com.ito.vip.service.TokenService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service(value = "tokenService")
public class TokenServiceImpl implements TokenService {

    private static Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private TokenDAO tokenDAO;

    @Autowired
    private TokenProperty tokenProperty;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    @CommonTransactional
    public Token createToken(String valueField, Long userId, String deviceId, Date loginDate, Date expireDate) {
        Token token = null;
        // Step1: create in database
        if(!this.isTokenExisted(valueField)){
            token = new Token();
            token.setValueField(valueField);
            token.setUserId(userId);
            token.setDeviceId(deviceId);
            token.setLoginDate(loginDate);
            token.setExpireDate(expireDate);
            token = tokenDAO.create(token);
            logger.info("create token into store, and the token is : " + valueField);
            // TODO Step2: create redis
            redisTemplate.opsForValue().set(valueField, token);
            logger.debug("create JWT token into redis, and the token is : " + valueField);
        }
        return token;
    }

    @Override
    @CommonTransactional
    public Token extendToken(Token token) {
        //Step1: generate the updated token value
        Long userId = token.getPkId();
        String deviceId = token.getDeviceId();
        String loginDate = DateFormatUtil.convertDateToString(token.getLoginDate());

        long currentTime =  new Date().getTime();
        Long expirationDaysLong = tokenProperty.getExpiration();
        long  expirationTime = expirationDaysLong * 24 * 60 * 60 * 1000;
        Date updatedExpireDate = new Date(currentTime + expirationTime);
        String expireDate = DateFormatUtil.convertDateToString(updatedExpireDate);

        String oldTokenValue = token.getValueField();
        String updatedTokenValue = RequestUtil.generateToken(userId.toString(), deviceId, loginDate, expireDate);

        //Stept2: persist the updated token in store
        token.setExpireDate(updatedExpireDate);
        token.setValueField(updatedTokenValue);
        tokenDAO.update(token);

        //  Step3: update token in redis
        redisTemplate.opsForValue().set(oldTokenValue, null, 1, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(updatedTokenValue, token);
        return token;
    }

    @Override
    public Token getTokenByValue(String valueField) {
        Token token = null;
        // If token exists, it should in redis, due to isTokenExisted() method will load token into redis.
        Boolean result = this.isTokenExisted(valueField);
        if (result) {
            Object cachedValue = redisTemplate.opsForValue().get(valueField);
            if (cachedValue != null) {
                if(cachedValue instanceof Token){
                    token = ((Token)cachedValue);
                }
            }
        }
        return token;
    }

    @Override
    @CommonTransactional
    public void deleteTokenByValue(String valueField) {
        //Step1: delete in database
        tokenDAO.deleteTokenByValue(valueField);
        logger.info("delete  token from store, and the token is : " + valueField);

        // TODO Step2: delete in redis
        redisTemplate.opsForValue().set(valueField, null, 0, TimeUnit.SECONDS);

        logger.info("delete token from redis, and the token is : " + valueField);
    }

    @Override
    public Boolean isTokenExisted(String tokenValue) {
        String uri = StringUtils.EMPTY;
        if(ContextUtil.getWebRequest() != null){
            uri = ContextUtil.getWebRequest().getRequestURI();
        }

        Boolean result = Boolean.FALSE;
        Object cachedValue = redisTemplate.opsForValue().get(tokenValue);
        if (cachedValue != null) {
            result = Boolean.TRUE;
            if (cachedValue instanceof Token) {
                tokenValue = ((Token)cachedValue).getValueField();
            }
            logger.info("get token from local cache, and the token is : " + tokenValue + " request uri is : " + uri);
        } else {
            Token tokenEntity = tokenDAO.getTokenByValue(tokenValue);
            if (tokenEntity != null) {
                result = Boolean.TRUE;
                tokenValue = tokenEntity.getValueField();
                redisTemplate.opsForValue().set(tokenValue, tokenEntity);
                logger.info("get token from store, and the token is : " + tokenValue + " request uri is : " + uri);
            } else {
                result = Boolean.FALSE;
            }
        }
        return result;
    }
}
