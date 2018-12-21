package com.ito.vip.common.util;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtil {

    private static final String APP_LOGOUT_URI = "/users/logout";

    private RequestUtil(){

    }

    public static Boolean isStaticResourceRequest(HttpServletRequest request){
        Boolean result  = Boolean.FALSE;
        String requestURI = request.getRequestURI();
        if(requestURI.contains("/js") || requestURI.endsWith(".js")){
            result = Boolean.TRUE;
        }
        if(requestURI.contains("/css") || requestURI.endsWith(".css")){
            result = Boolean.TRUE;
        }
        if(requestURI.contains("/html") || requestURI.endsWith(".html")){
            result = Boolean.TRUE;
        }
        if(requestURI.contains("/image") || requestURI.contains("/images")){
            result = Boolean.TRUE;
        }
        return result;
    }

    public static Boolean isAPPLogoutRequest(HttpServletRequest request){
        Boolean result  = Boolean.FALSE;
        String requestURI = request.getRequestURI();
        if(requestURI.equalsIgnoreCase(APP_LOGOUT_URI) || requestURI.contains(APP_LOGOUT_URI) || requestURI.endsWith(APP_LOGOUT_URI)){
            result = Boolean.TRUE;
        }
        return result;
    }

    public static String generateToken(String userId, String deviceId, String loginDate, String expireDate){
        return MD5Util.getMD5EncodeString(userId + "###" + deviceId + "###" + loginDate + "###" + expireDate);
    }
}
