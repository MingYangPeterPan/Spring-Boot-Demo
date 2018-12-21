package com.ito.vip.common.web.component;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class APIVersionHeaderFilterConfiguration {

    public static final Integer ORDER = FirstFilterConfiguration.ORDER + 1;

    @Bean
    public FilterRegistrationBean apiVersionHeaderFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new APIVersionHeaderFilter());
        registration.addUrlPatterns("/*");
        registration.setName("apiVersionHeaderFilter");
        registration.setOrder(ORDER);
        return registration;
    }
}
