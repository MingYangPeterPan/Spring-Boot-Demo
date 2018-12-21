package com.ito.vip;

import com.ito.vip.common.annotation.*;
import com.ito.vip.common.util.listener.ApplicationEnvironmentPreparedListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJdbcTransaction
@EnableStreamUpload
@EnableAPIVersionHeader
@EnableFirstFilter
@EnableSpringUtil
@EnableAPIDocumentation
@EnableProperties
@EnableServerRedirect
@SpringBootApplication
@EnableAuthenticationFilter
public class Application {

    public static void main( String[] args ){
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(new ApplicationEnvironmentPreparedListener());
        springApplication.run(args);
    }
}
