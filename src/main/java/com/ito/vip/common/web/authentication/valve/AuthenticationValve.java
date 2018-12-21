package com.ito.vip.common.web.authentication.valve;

import com.ito.vip.common.util.ContextUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class AuthenticationValve {

    private AuthenticationValve nextValve;

    public void setNextValve(AuthenticationValve nextValve) {
        this.nextValve = nextValve;
    }

    public AuthenticationValve getNextValve() {
        return nextValve;
    }

    public AuthenticationResult doAuthentication() {
        HttpServletRequest request = ContextUtil.getWebRequest();
        AuthenticationResult result = AuthenticationResult.fail();
        result = this.processInnerAuthentication(request);
        if(result!=null && result.isSuccess()){
            AuthenticationValve nextValve = this.getNextValve();
            if(nextValve!=null){
                result = nextValve.doAuthentication();
            }
        }
        return result;
    }

    protected abstract AuthenticationResult processInnerAuthentication(HttpServletRequest request);

    public static class AuthenticationResult {

        private Boolean isSuccess;

        private Object failMessage;

        private AuthenticationResult(Boolean isSuccess, Object failMessage) {
            this.isSuccess = isSuccess;
            this.failMessage = failMessage;
        }

        public Boolean isSuccess() {
            return isSuccess;
        }

        public Object getFailMessage() {
            return failMessage;
        }

        public static AuthenticationResult fail(Object failMessage) {
            return new AuthenticationResult(Boolean.FALSE, failMessage);
        }

        public static AuthenticationResult fail() {
            return new AuthenticationResult(Boolean.FALSE, StringUtils.EMPTY);
        }

        public static AuthenticationResult success() {
            return new AuthenticationResult(Boolean.TRUE, StringUtils.EMPTY);
        }
    }
}
