package com.ito.vip.common.web.log;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.JsonUtil;
import com.ito.vip.common.web.WebContext;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import com.ito.vip.model.JwtUser;
import com.ito.vip.model.Token;
import com.ito.vip.service.TokenService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;


@Aspect
@Component
public class APILogAspect{

    private final static Logger logger = LoggerFactory.getLogger(APILogAspect.class);

    @Autowired
    private TokenService tokenService;

    /**
     * Scan all public methods under the specified packages' controller
     */
    @Pointcut("execution(public * com.ito.vip.controller..*.*(..))")
    public void cut() {

    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = null;
        try {
            object = joinPoint.proceed();
            this.logRequest(joinPoint);
        } catch (Exception e) {
            logger.error("Controller调用异常", e);
            throw e;
        }
        return object;
    }

    /**
     * Log api request into database and api-log file
     *
     * @param joinPoint
     */
    private void logRequest(ProceedingJoinPoint joinPoint) {
        WebContext context = ContextUtil.getWebContext();
        HttpServletRequest request = context.getRequest();

        String apiClass = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] parameters = joinPoint.getArgs();

        // Construct header
        HashMap<String, Object> headers = new HashMap<String, Object>();
        Enumeration<String> headerList = request.getHeaderNames();
        while (headerList.hasMoreElements()) {
            String headerName = (String) headerList.nextElement();
            headers.put(headerName.toLowerCase(), request.getHeader(headerName));
        }

        String deviceId = request.getHeader(RequestHeaderKeyConstants.DEVICE_ID);
        Long userId = null;
        if (deviceId == null) {
            JwtUser userDetail = ContextUtil.getAttribute(ContextUtil.USER);
            userId = null == userDetail ? null : userDetail.getPkId();
        } else {
            String tokenValue = request.getHeader(RequestHeaderKeyConstants.TOKEN);
            if(null == tokenValue){
                tokenValue = request.getParameter(RequestHeaderKeyConstants.TOKEN);
            }
            if(null != tokenValue && !tokenValue.isEmpty()){
                // Get user information by token value.
                Token token = tokenService.getTokenByValue(tokenValue);
                if(null != token){
                    userId = token.getUserId();
                }
            }
        }

        ArrayList<Object> parameterList = new ArrayList<Object>();
        for(Object parameter : parameters){
            if(!(parameter instanceof HttpServletRequest) && !(parameter instanceof HttpServletResponse)){
                parameterList.add(parameter);
            }
        }

        String ipAddress = request.getRemoteAddr();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String httpMethod = request.getMethod();

        // Log into api-log file
        StringBuilder apiStrb = new StringBuilder();
        apiStrb.append("UserId"        +":" + userId +" ");
        apiStrb.append("MethodName"    +":" + methodName +" ");
        apiStrb.append("IPAddress"    +":" + ipAddress +" ["+request.getHeader("x-forwarded-for")+"]" +" ");

        String parameterStr = StringUtils.EMPTY;
        for (Object parameter : parameterList){
            try {
                String paraJson = JsonUtil.convertObjectToJSON(parameter);
                parameterStr = parameterStr
                    + ( paraJson.length() < 500 ? paraJson : "==" + paraJson.length()+"BYTES==" )
                    + ";";
            } catch (Exception e){
                e.printStackTrace();
                logger.error(apiStrb.toString() + e);
            }
        }

        logger.info(apiStrb.toString()+"Parameter"+"["+parameterStr+"]");
    }

}
