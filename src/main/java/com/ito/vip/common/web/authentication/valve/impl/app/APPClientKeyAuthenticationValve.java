package com.ito.vip.common.web.authentication.valve.impl.app;

import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.common.web.authentication.valve.AuthenticationValve;
import com.ito.vip.constants.ErrorCodeConstants;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import com.ito.vip.property.ClientKeyProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;


public class APPClientKeyAuthenticationValve extends AuthenticationValve{

    @Autowired
    private ClientKeyProperty clientKey;

    @Override
    protected AuthenticationResult processInnerAuthentication(HttpServletRequest request) {
        AuthenticationResult result = AuthenticationResult.fail();
        if(!this.isClientKeyExistsInRequest(request)){
            //Client not exist in request header
            MessageEntry message = Message.error(ErrorCodeConstants.CLIENT_KEY_IS_REQUIRED, "client key is required");
            result = AuthenticationResult.fail(message);
        }else{
            //Client exists in request header but is not configured in system
            if(!this.isClientValidInSystem(request)){
                MessageEntry message = Message.error(ErrorCodeConstants.CLIENT_KEY_IS_VALID, "client key is invalid");
                result = AuthenticationResult.fail(message);
            }else{
                //Client exists in request header and configured in system
                result = AuthenticationResult.success();
            }
        }
        return result;
    }

    private Boolean isClientKeyExistsInRequest(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        String clientKey = request.getHeader(RequestHeaderKeyConstants.CLIENT_KEY);
        if(!StringUtils.isEmpty(clientKey)){
            result = Boolean.TRUE;
        }
        return result;
    }

    private Boolean isClientValidInSystem(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        String clientKeyValue = request.getHeader(RequestHeaderKeyConstants.CLIENT_KEY);
        if(!StringUtils.isEmpty(clientKeyValue)){
            if(clientKey.containsKey(clientKeyValue)){
                result = Boolean.TRUE;
            }
        }
        return result;
    }
}
