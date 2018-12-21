package com.ito.vip.common.web.authentication.valve.impl.web;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.JwtUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.common.web.authentication.valve.AuthenticationValve;
import com.ito.vip.constants.ErrorCodeConstants;
import com.ito.vip.model.JwtUser;
import com.ito.vip.property.AuthenticationIgnoreProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;


public class WebAuthenticationValve extends AuthenticationValve{

    @Autowired
    private AuthenticationIgnoreProperty authenticationIgnore;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    protected AuthenticationResult processInnerAuthentication(HttpServletRequest request) {
        AuthenticationResult result = AuthenticationResult.fail();
        String authToken = request.getHeader(jwtHeader);
        if (authenticationIgnore.isWebIgnore(request.getRequestURI())) {
            result = AuthenticationResult.success();
        }else{
            if (null == authToken) {
                 MessageEntry message = Message.error(ErrorCodeConstants.TOKEN_IS_REQUIRED, "your jwt token is required");
                 result = AuthenticationResult.fail(message);
            } else {

                if (null != redisTemplate.opsForValue().get(authToken)) {
                    if (jwtUtil.isTokenExpired(authToken)) {
                        MessageEntry message = Message.error(ErrorCodeConstants.TOKEN_IS_INVALID, "your jwt token is invalid");
                        result = AuthenticationResult.fail(message);
                    } else {
                        JwtUser user = jwtUtil.decodeToken(authToken);

                        ContextUtil.setAttribute(ContextUtil.USER, user);
                        ContextUtil.setAttribute(ContextUtil.JWT, authToken);

                        result = AuthenticationResult.success();
                    }
                } else {
                    MessageEntry message = Message.error(ErrorCodeConstants.TOKEN_IS_INVALID, "your jwt token is invalid");
                    result = AuthenticationResult.fail(message);
                }
            }
        }
        return result;
    }
}
