package com.ito.vip.common.web.component;

import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.common.util.UserAgentUtil;
import com.ito.vip.constants.APIVersionConstants;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class APIVersionHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
         HttpServletRequest request = (HttpServletRequest)servletRequest;
         if(RequestUtil.isStaticResourceRequest(request)){
             chain.doFilter(servletRequest, servletResponse);
         }else{
             try{
                 if(UserAgentUtil.isFromMobileAPP(request)){
                     HeaderHttpServletRequestWrapper wrapperedRequest = new HeaderHttpServletRequestWrapper(request);
                     this.addAPIVersionHeaderIfNecessary(wrapperedRequest);
                     request = wrapperedRequest;
                 }
             }finally{
                 chain.doFilter(request, servletResponse);
             }
         }
    }

    private void addAPIVersionHeaderIfNecessary(HeaderHttpServletRequestWrapper request){
        String apiVersionHeader = request.getHeader(RequestHeaderKeyConstants.API_VERSION);
        if(StringUtils.isEmpty(apiVersionHeader)){
            request.addHeader(RequestHeaderKeyConstants.API_VERSION, APIVersionConstants.API_VERSION_1_0);
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
