package com.ito.vip.common.web.component;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Any servlet filter, listener can be configure here
 */
public class FirstFilterConfiguration {

    public static final Integer ORDER = Integer.MIN_VALUE;

    @Bean
    public FilterRegistrationBean firstFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new FirstFilter());
        registration.addUrlPatterns("/*");
        registration.setName("firstFilter");
        registration.setOrder(ORDER);
        return registration;
    }
}
