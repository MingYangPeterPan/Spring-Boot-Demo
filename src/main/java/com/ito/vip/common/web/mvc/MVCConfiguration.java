package com.ito.vip.common.web.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MVCConfiguration extends WebMvcConfigurerAdapter {

     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/pages/");
//         registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//         registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/pages");
//         registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
//         registry.addResourceHandler("/pages/**").addResourceLocations("classpath:/pages/");
//         registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//
//         registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//         registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
         super.addResourceHandlers(registry);


     }

}
