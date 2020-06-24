package com.hyhello.priceless.support;

import com.hyhello.priceless.fileconfig.CommonConfig;
import com.hyhello.priceless.fileconfig.TokenConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
public class BeanSupport implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    public static CommonConfig getCommonConfig(){
        return context.getBean("commonConfig",CommonConfig.class);
    }
    public static TokenConfig getTokenConfig(){
        return context.getBean("tokenConfig", TokenConfig.class);
    }

}
