package com.dinh.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LogisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsServiceApplication.class, args);
    }
    
    @Bean
    public WebMvcConfigurer tokenInterceptorConfigurer() {
        return new WebMvcConfigurer() {
            @Autowired
            private TokenInterceptor tokenInterceptor;
            
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tokenInterceptor)
                        .addPathPatterns("/api/**")
                        .excludePathPatterns("/api/mobile/**")
                        .excludePathPatterns("/api/auth/**");
            }
        };
    }

}
