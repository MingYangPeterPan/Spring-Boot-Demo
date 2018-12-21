package com.ito.vip.common.web.authentication.valve.impl.app;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.common.web.authentication.valve.AuthenticationValve;
import com.ito.vip.constants.ErrorCodeConstants;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import com.ito.vip.model.Token;
import com.ito.vip.property.AuthenticationIgnoreProperty;
import com.ito.vip.service.TokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;


public class APPTokenAuthenticationValve extends AuthenticationValve {

    @Autowired
    private AuthenticationIgnoreProperty authenticationIgnore;

    @Autowired
    private TokenService tokenService;

    @Override
    protected AuthenticationResult processInnerAuthentication(HttpServletRequest request) {
        AuthenticationResult result = AuthenticationResult.fail();
        if (authenticationIgnore.isTokenIgnore(request.getRequestURI())) {
            result = AuthenticationResult.success();
        } else {
            result = this.processTokenAuthentication(request);
        }
        return result;
    }

    private AuthenticationResult processTokenAuthentication(HttpServletRequest request){
        AuthenticationResult result = AuthenticationResult.fail();
        if (!this.isTokenExistsInRequest(request)) {
            //Token not exist in request header
            MessageEntry message = Message.error(ErrorCodeConstants.TOKEN_IS_REQUIRED, "token is required");
            result = AuthenticationResult.fail(message);
        } else {
            String tokenValue = request.getHeader(RequestHeaderKeyConstants.TOKEN);
            Boolean tokenExistInSystem = tokenService.isTokenExisted(tokenValue);
            if (!tokenExistInSystem) {
                // Token exist in request header, but invalid, means does not match any item in token store
                MessageEntry message = Message.error(ErrorCodeConstants.TOKEN_IS_INVALID, "token is invalid");
                result = AuthenticationResult.fail(message);
            } else {
                if (!RequestUtil.isAPPLogoutRequest(request)) {
                    //None logout request need validate whether token is expired or not
                    Token token = tokenService.getTokenByValue(tokenValue);
                    if (null == token || token.isTokenExpired()) {
                        //Token exist in request header, valid, but expired
                        MessageEntry message = Message.error(ErrorCodeConstants.TOKEN_IS_EXPIRED, "token is expired");
                        result = AuthenticationResult.fail(message);
                    } else {
                        //Token exist in request header, valid, and not expired
                        result = AuthenticationResult.success();
                    }
                } else {
                    //logout request just need token exists is OK
                    result = AuthenticationResult.success();
                }

                //Bind token to context if token is authenticated success
                if (result.isSuccess()) {
                    ContextUtil.setAttribute(ContextUtil.TOKEN, tokenService.getTokenByValue(tokenValue));
                }
            }
        }
        return result;

    }

    private Boolean isTokenExistsInRequest(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        String tokenValue = request.getHeader(RequestHeaderKeyConstants.TOKEN);
        if(!StringUtils.isEmpty(tokenValue)){
            result = Boolean.TRUE;
        }
        return result;
    }

}
