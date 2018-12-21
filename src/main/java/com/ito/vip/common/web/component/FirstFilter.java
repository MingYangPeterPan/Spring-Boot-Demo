package com.ito.vip.common.web.component;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.ExceptionUtil;
import com.ito.vip.common.util.JsonUtil;
import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.common.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirstFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(FirstFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
         HttpServletRequest request = (HttpServletRequest)servletRequest;
         HttpServletResponse response = (HttpServletResponse)servletResponse;
         if(RequestUtil.isStaticResourceRequest(request)){
             filterChain.doFilter(servletRequest, servletResponse);
         }else{
             try{
                 WebContext webContext = new WebContext(request, response);
                 ContextUtil.setAttribute(ContextUtil.WEB_CONTEXT, webContext);
                 filterChain.doFilter(servletRequest, servletResponse);
            }catch (Exception e) {
                //Any exception happens will build as JSON message with exception details stack trace
                processException(e, response);
            }finally{
                ContextUtil.clearContext();
            }
         }
    }

    protected void processException(Exception exception, HttpServletResponse response){
         //Step1 log the error message
         String errorMessage = ExceptionUtil.getExceptionStackTrace(exception);
         log.error(errorMessage);

         //Step2 response error message
         MessageEntry messageEntory = Message.error(errorMessage);
         String message = JsonUtil.convertObjectToJSON(messageEntory);
         try {
            response.getWriter().println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
