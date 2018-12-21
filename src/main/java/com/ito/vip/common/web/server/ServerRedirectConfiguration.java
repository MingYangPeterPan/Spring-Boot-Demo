package com.ito.vip.common.web.server;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class ServerRedirectConfiguration {

    @Bean
    public TomcatServletWebServerFactory tomcatContainerFactory(Connector httpConnector){
        TomcatServletWebServerFactory tomcatContainerFactory = new TomcatContainerFactory();
        //Step1: set connector customizer to redirect
        tomcatContainerFactory.addAdditionalTomcatConnectors(httpConnector);
        //Step2: set error page
//        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/not-found.html");
//        tomcatContainerFactory.addErrorPages(error404Page);
        return tomcatContainerFactory;
    }


    /**
     * Open 80 port that support HTTP request
     */
    @Bean
    public Connector httpConnector(Environment env) {
        //Step1: Open 80 port that support HTTP request
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(5679);
        connector.setSecure(Boolean.FALSE);
        //Step1: Redirect HTTP port to configured HTTPS port as server.port
//        String redirectPort = env.getProperty("server.port");
//        if(!StringUtils.isEmpty(redirectPort)){
//            Integer targetPort = Integer.valueOf(redirectPort);
//            connector.setRedirectPort(targetPort);
//        }
        return connector;
    }
}
