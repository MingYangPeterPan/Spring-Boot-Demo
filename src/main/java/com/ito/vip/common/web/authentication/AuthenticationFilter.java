package com.ito.vip.common.web.authentication;

import com.ito.vip.common.util.JsonUtil;
import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.common.util.UserAgentUtil;
import com.ito.vip.common.web.authentication.valve.AuthenticationValve;
import com.ito.vip.common.web.authentication.valve.AuthenticationValve.AuthenticationResult;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private AuthenticationValve webAuthenticationChain;

    private AuthenticationValve appAuthenticationChain;

    public void setWebAuthenticationChain(AuthenticationValve webAuthenticationChain) {
        this.webAuthenticationChain = webAuthenticationChain;
    }

    public void setAppAuthenticationChain(AuthenticationValve appAuthenticationChain) {
        this.appAuthenticationChain = appAuthenticationChain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
         HttpServletRequest request = (HttpServletRequest)servletRequest;
         HttpServletResponse response = (HttpServletResponse)servletResponse;
         if(RequestUtil.isStaticResourceRequest(request)){
             // Static resource result request does not need do authentication
             filterChain.doFilter(servletRequest, servletResponse);
         }else{
             // Do different authentication from web access and APP access
             AuthenticationResult result = this.processAuthentication(request);
             if (result.isSuccess()) {
                 // Allow the filter if authentication success
                 filterChain.doFilter(request, response);
             } else {
                 //In fail case, return the error message
                 if (null == request.getHeader(RequestHeaderKeyConstants.API_VERSION) || UserAgentUtil.isBrowserAjaxRequest(request)) {
                     Object message = result.getFailMessage();
                     this.responseWithJSONMessage(response, message);
                 } else if (UserAgentUtil.isFromBrowserDirectlyRequest(request)) {
                     response.sendRedirect("/index.html");
                 }
             }
         }
    }

    private AuthenticationResult processAuthentication(HttpServletRequest request){
        AuthenticationResult result = AuthenticationResult.fail();
//        if (UserAgentUtil.isFromBrowser(request)) {
//            result = webAuthenticationChain.doAuthentication();
//        } else if(UserAgentUtil.isFromMobileAPP(request)) {
//             result = appAuthenticationChain.doAuthentication();
//        }
        if (null == request.getHeader(RequestHeaderKeyConstants.API_VERSION)) {
            result = webAuthenticationChain.doAuthentication();
        } else {
            result = appAuthenticationChain.doAuthentication();
        }
        return result;
    }

    private void responseWithJSONMessage(HttpServletResponse response, Object message){
        String finalMessage = JsonUtil.convertObjectToJSON(message) ;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, finalMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Empty implementation here
    }


    @Override
    public void destroy() {
        //Empty implementation here
    }
}
