package com.ito.vip.common.web.component;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class HeaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public HeaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void addHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if(headerValue==null){
            headerValue = super.getHeader(name);
        }
        return headerValue;
    }

    @Override
    public Enumeration<String> getHeaderNames(){
        Set<String> set = new HashSet<String>(customHeaders.keySet());
        Enumeration<String> enumeration = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String item = enumeration.nextElement();
            set.add(item);
        }
        return Collections.enumeration(set);
    }
}
