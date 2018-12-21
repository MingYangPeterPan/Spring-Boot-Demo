package com.ito.vip.property;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationIgnoreProperty {

    @Value("${authentication.web.ignore}")
    private String webIgnore;

    @Value("${authentication.token.ignore}")
    private String tokenIgnore;

    private List<String> targetWebIgnore;

    private List<String> targetTokenIgnore;

    public Boolean isWebIgnore(String uri){
        Boolean result = Boolean.FALSE;
        if(!StringUtils.isEmpty(uri)){
            this.parseWebIgnore();
            for(String item : targetWebIgnore){
                if(item.equalsIgnoreCase(uri)||uri.endsWith(item)||uri.contains(item)){
                    result = Boolean.TRUE;
                    break;
                }
            }
        }
        return result;
    }

    public Boolean isTokenIgnore(String uri){
        Boolean result = Boolean.FALSE;
        if(!StringUtils.isEmpty(uri)){
            this.parseTokenIgnore();
            for(String item : targetTokenIgnore){
                if(item.equalsIgnoreCase(uri)||uri.endsWith(item)||uri.contains(item)){
                    result = Boolean.TRUE;
                    break;
                }
            }
        }
        return result;
    }

    private void parseWebIgnore(){
        if(targetWebIgnore==null){
            targetWebIgnore = new ArrayList<>();
            if(!StringUtils.isEmpty(webIgnore)){
                String[] items = webIgnore.split(",");
                for(String path : items){
                    path = path.trim();
                    targetWebIgnore.add(path);
                }
            }
        }
    }

    private void parseTokenIgnore(){
        if(targetTokenIgnore==null){
            targetTokenIgnore = new ArrayList<>();
            if(!StringUtils.isEmpty(tokenIgnore)){
                String[] items = tokenIgnore.split(",");
                for(String path : items){
                    path = path.trim();
                    targetTokenIgnore.add(path);
                }
            }
        }
    }
}
