package com.ito.vip.common.util;


public final class StringConvertUtil {
    
    private StringConvertUtil(){
        
    }
    
    public static Long convertStringToLong(String value){
        Long result = null;
        try{
            result = Long.valueOf(value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
     }
    
    public static Boolean convertStringToBolean(String value){
        Boolean result = Boolean.FALSE;
        try{
            result = Boolean.valueOf(value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static Integer convertStringToInteger(String value){
        Integer result = null;
        try{
            result = Integer.valueOf(value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static Float convertStringToFloat(String value){
        Float result = null;
        try{
            result = Float.valueOf(value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static Double convertStringToDouble(String value){
        Double result = null;
        try{
            result = Double.valueOf(value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
