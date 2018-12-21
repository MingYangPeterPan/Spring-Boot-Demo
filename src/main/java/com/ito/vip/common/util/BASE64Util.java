package com.ito.vip.common.util;

import java.util.Base64;

public final class BASE64Util {
    
    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    private BASE64Util(){
        
    }
    
    public static String encodeString(String content){
        String result = null;
        if(content!=null){
            byte[] byteContent = content.getBytes();
            result= encoder.encodeToString(byteContent);
        }
        return result;
    }
    
    public static String decodeString(String content){
        String result = null;
        if(content!=null){
            try {
                byte[] byteContent = decoder.decode(content);
                result = new String(byteContent);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
