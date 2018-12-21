package com.ito.vip.property;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class ClientKeyProperty {

    @Value("${client.keys}")
    private String clientKeys;

    private List<String> targetKeys = null;

    public List<String> getClientKeys() {
        this.parseClientKeys();
        return targetKeys;
    }

    public Boolean containsKey(String key){
        this.parseClientKeys();
        return targetKeys.contains(key);
    }

    private void parseClientKeys(){
        if (targetKeys == null) {
            targetKeys = new ArrayList<>();
            if (!StringUtils.isEmpty(clientKeys)) {
                String[] keys = clientKeys.split(",");
                for (String key : keys) {
                    key = key.trim();
                    targetKeys.add(key);
                }
            }
        }
    }
}
