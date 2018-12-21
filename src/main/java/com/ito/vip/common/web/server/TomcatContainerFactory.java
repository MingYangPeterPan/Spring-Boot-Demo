package com.ito.vip.common.web.server;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

public class TomcatContainerFactory extends TomcatServletWebServerFactory {

    @Override
    protected void postProcessContext(Context context) {
        // Set this securityConstraint, then can redirect
//        SecurityConstraint securityConstraint = new SecurityConstraint();
//        securityConstraint.setUserConstraint("CONFIDENTIAL");
//        SecurityCollection collection = new SecurityCollection();
//        collection.addPattern("/*");
//        securityConstraint.addCollection(collection);
//        context.addConstraint(securityConstraint);
    }
}
