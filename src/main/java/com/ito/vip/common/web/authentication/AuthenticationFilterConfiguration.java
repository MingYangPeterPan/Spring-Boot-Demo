package com.ito.vip.common.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.ito.vip.common.web.authentication.valve.AuthenticationValve;
import com.ito.vip.common.web.authentication.valve.impl.app.APPClientKeyAuthenticationValve;
import com.ito.vip.common.web.authentication.valve.impl.app.APPTokenAuthenticationValve;
import com.ito.vip.common.web.authentication.valve.impl.web.WebAuthenticationValve;
import com.ito.vip.common.web.upload.MutiPartRequestPreProcessFilterConfiguration;

public class AuthenticationFilterConfiguration {

    public static final Integer ORDER = MutiPartRequestPreProcessFilterConfiguration.ORDER + 1;

    @Autowired
    @Qualifier(value = "webAccessAuthenticationChain")
    private AuthenticationValve webAccessChain;

    @Autowired
    @Qualifier(value = "appAccessAuthenticationChain")
    private AuthenticationValve appAccessChain;

    @Bean
    public FilterRegistrationBean authenticationFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAppAuthenticationChain(appAccessChain);
        filter.setWebAuthenticationChain(webAccessChain);
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("authenticationFilter");
        registration.setOrder(ORDER);
        return registration;
    }

    @Bean(value = "webAccessAuthenticationChain")
    public AuthenticationValve webAccessAuthenticationChain(){
        AuthenticationValve webValve = new WebAuthenticationValve();
        return webValve;
    }

    @Bean(value = "appAccessAuthenticationChain")
    public AuthenticationValve appAccessAuthenticationChain(APPTokenAuthenticationValve tokenValve){
        AuthenticationValve clientKeyValve = new APPClientKeyAuthenticationValve();
        clientKeyValve.setNextValve(tokenValve);
        return clientKeyValve;
    }

    @Bean
    public APPTokenAuthenticationValve appTokenAuthenticationValve(){
        APPTokenAuthenticationValve tokenValve = new APPTokenAuthenticationValve();
        return tokenValve;
    }
}
