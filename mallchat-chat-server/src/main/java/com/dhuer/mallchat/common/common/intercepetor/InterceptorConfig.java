package com.dhuer.mallchat.common.common.intercepetor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/28
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Autowired
    private CollectorInterceptor collectorInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Note：拦截器顺序不能改变
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/capi/**");
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/capi/**");
    }
}
