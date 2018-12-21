package com.ito.vip.common.web.upload;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.ito.vip.common.util.ExceptionUtil;
import com.ito.vip.common.util.RequestUtil;

public class MutiPartRequestPreProcessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if(RequestUtil.isStaticResourceRequest(request)){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            try{
                 this.preProcessMutiPartRequest(request);
            }catch (Exception exception) {
                throw ExceptionUtil.wrapAsRuntimeException(exception);
            }finally {
                 filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @SuppressWarnings("unused")
    private void preProcessMutiPartRequest(HttpServletRequest request){
        try{
            String requestMethod = request.getMethod();
            String requestURI = request.getRequestURI();
            String contextType = request.getContentType();
            if("post".equalsIgnoreCase(requestMethod) && requestURI.endsWith("/resources/upload")){
                if(!StringUtils.isEmpty(contextType)){
                    if(contextType.toLowerCase().contains("multipart/form-data")){
                        int avaiable = request.getInputStream().available();
                    }
                }
            }
        }catch(Exception excaption){
            throw ExceptionUtil.wrapAsRuntimeException(excaption);
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
