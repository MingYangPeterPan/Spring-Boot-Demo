package com.ito.vip.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtil {

     private ExceptionUtil(){

     }

     public static String getExceptionStackTrace(Throwable exception) {
         String result = StringUtils.EMPTY;
         if(exception!=null){
             StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter);
             exception.printStackTrace(printWriter);
             result = stringWriter.toString();
         }
         return result;
     }

     public static RuntimeException wrapAsRuntimeException(Exception exception) {
         RuntimeException result = null;
         Boolean shouldWrap = Boolean.TRUE;
         if(exception instanceof RuntimeException){
             shouldWrap = Boolean.FALSE;
             result = (RuntimeException)exception;
         }
         if(shouldWrap){
             result = new RuntimeException(exception);
         }
         return result;
     }
}
