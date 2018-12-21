package com.ito.vip.common.util;

import com.ito.vip.common.web.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public final class ContextUtil {
    
    public static final String WEB_CONTEXT = "webContext";
    public static final String TOKEN = "token";
    public static final String JWT = "jwt-token";
    public static final String USER = "user";

    private static ThreadLocal<ContextUtil> threadLocal = new ThreadLocal<ContextUtil>();
    private HashMap<String, Object> innerMap;
    
    private ContextUtil(){
        innerMap = new HashMap<String, Object>();
    }
    
    private HashMap<String, Object> getInnerMap() {
        return innerMap;
    }

    
    private static ContextUtil getContext(){
        ContextUtil context = threadLocal.get();
        if(context==null){
            context = new ContextUtil();
            threadLocal.set(context);
        }
        return context;
    }
    
    public static void setAttribute(String key, Object value){
        ContextUtil context = ContextUtil.getContext();
        HashMap<String, Object> innerMap = context.getInnerMap();
        if(innerMap!=null){
            innerMap.put(key, value);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String key){
        T target = null;
        ContextUtil context = threadLocal.get();
        if(context!=null){
            HashMap<String, Object> innerMap = context.getInnerMap();
            if(innerMap!=null && innerMap.containsKey(key)){
                target = (T)innerMap.get(key);
            }
        }
        return target;
    }
    
    public static void removeAttribute(String key){
        ContextUtil context = threadLocal.get();
        if(context!=null){
            HashMap<String, Object> innerMap = context.getInnerMap();
            if(innerMap!=null && innerMap.containsKey(key)){
                innerMap.remove(key);
            }
        }
    }
    
    public static void clearContext(){
        ContextUtil context = threadLocal.get();
        if(context!=null){
            threadLocal.remove();
            context.getInnerMap().clear();
            context = null;
        }
    }
    
    public static WebContext getWebContext(){
        WebContext webContext = ContextUtil.getAttribute(WEB_CONTEXT);
        return webContext;
    }
    
    public static HttpServletRequest getWebRequest(){
        HttpServletRequest request = null;
        WebContext webContext = ContextUtil.getWebContext();
        if(webContext!=null){
            request = webContext.getRequest();
        }
        return request;
    }
    
    public static HttpServletResponse getWebResponse(){
        HttpServletResponse response = null;
        WebContext webContext = ContextUtil.getWebContext();
        if(webContext!=null){
            response = webContext.getResponse();
        }
        return response;
    }
}
