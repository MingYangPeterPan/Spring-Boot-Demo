package com.ito.vip.common.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public final class UserAgentUtil {

    private static final String USER_AGENT = "User-Agent";

    public static final String AJAX_REQUEST_HEADER = "X-Requested-With";

    public static final String AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";

    UserAgentUtil(){

    }

    public static Boolean isFromBrowser(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        UserAgent parsedAgent = UserAgentUtil.getUserAgent(request);
        Browser browser = parsedAgent.getBrowser();
        if(!Browser.UNKNOWN.equals(browser)){
            result = Boolean.TRUE;
        }
        return result;
    }

    public static Boolean isFromBrowserDirectlyRequest(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        Boolean isBrowserRequest = UserAgentUtil.isFromBrowser(request);
        if(isBrowserRequest){
            if(!UserAgentUtil.isBrowserAjaxRequest(request)){
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    public static Boolean isBrowserAjaxRequest(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        Boolean isBrowserRequest = UserAgentUtil.isFromBrowser(request);
        if(isBrowserRequest){
            String headerValue = request.getHeader(AJAX_REQUEST_HEADER);
            if(!StringUtils.isEmpty(headerValue) && AJAX_REQUEST_HEADER_VALUE.equalsIgnoreCase(headerValue)){
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static Boolean isFromMobileAPP(HttpServletRequest request){
        Boolean result = Boolean.FALSE;
        if(!UserAgentUtil.isFromBrowser(request)){
            UserAgent parsedAgent = UserAgentUtil.getUserAgent(request);
            OperatingSystem operatingSystem = parsedAgent.getOperatingSystem();
            if(!OperatingSystem.UNKNOWN.equals(operatingSystem)){
                result = operatingSystem.isMobileDevice();
            }
        }
        return result;
    }

    private static UserAgent getUserAgent(HttpServletRequest request){
        String userAgent = StringUtils.EMPTY;
        userAgent = request.getHeader(UserAgentUtil.USER_AGENT);
        if(null == userAgent || StringUtils.isEmpty(userAgent)){
            userAgent = request.getParameter(UserAgentUtil.USER_AGENT);
        }
        UserAgent parsedAgent = UserAgent.parseUserAgentString(userAgent);
        return parsedAgent;
    }
}
