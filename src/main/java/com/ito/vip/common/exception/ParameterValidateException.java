package com.ito.vip.common.exception;

import java.util.HashMap;
import java.util.Map.Entry;

public class ParameterValidateException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE_SEPARATOR = ":";
    
    private static final String MESSAGE_PREFIX = "Parameters not valid ==> ";
    
    private HashMap<String, String> fields = new HashMap<String, String>();
    
    public HashMap<String, String> getFields() {
        return fields;
    }
    
    public void add(String field, String message){
        fields.put(field, message);
    }
    
    public Boolean hasError(){
        return !fields.isEmpty();
    }
    
    @Override
    public String getMessage(){
        String message = MESSAGE_PREFIX;
        if(!fields.isEmpty()){
            for(Entry<String, String> item : fields.entrySet()){
                String value = item.getValue();
                String field = value + MESSAGE_SEPARATOR;
                message += field;
            }
        }
        return message;
    }
}
