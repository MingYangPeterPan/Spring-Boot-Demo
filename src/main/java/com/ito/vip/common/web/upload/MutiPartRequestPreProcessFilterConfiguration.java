package com.ito.vip.common.web.upload;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.ito.vip.common.web.component.APIVersionHeaderFilterConfiguration;


public class MutiPartRequestPreProcessFilterConfiguration {

    public static final Integer ORDER = APIVersionHeaderFilterConfiguration.ORDER + 1;

    @Bean
    public FilterRegistrationBean mutiPartRequestPreProcessFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MutiPartRequestPreProcessFilter());
        registration.addUrlPatterns("/*");
        registration.setName("mutiPartRequestPreProcessFilter");
        registration.setOrder(ORDER);
        return registration;
    }
}
