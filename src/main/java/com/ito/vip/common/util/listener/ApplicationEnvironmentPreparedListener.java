package com.ito.vip.common.util.listener;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

public class ApplicationEnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String SPRING_APPLICATION_TIME_ZONE_KEY = "spring.application.timezone";
    private static final String TIME_ZONE_KEY = "user.timezone";
    private static final String DEFAULT_TIME_ZONE = "Asia/Shanghai";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Environment env = event.getEnvironment();
        this.setTimezone(env);
    }

    private void setTimezone(Environment env){
        String timeZone = env.getProperty(SPRING_APPLICATION_TIME_ZONE_KEY);
        if(StringUtils.isEmpty(timeZone)){
            timeZone = DEFAULT_TIME_ZONE;
        }
        System.setProperty(TIME_ZONE_KEY, timeZone);
    }

}
