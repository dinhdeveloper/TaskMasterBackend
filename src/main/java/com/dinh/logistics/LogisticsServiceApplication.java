package com.dinh.logistics;

import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class LogisticsServiceApplication {
	
	@Value("${app.firebase.sdk-file}")
    private String fireBaseServerKey;

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
                        .excludePathPatterns("/api/auth/login")
                        .excludePathPatterns("/api/test/**")
                        ;
            }
        };
    }
    
    @Bean
    public void FirebaseAppInitialization() {
    	String pathToServiceAccountKey = fireBaseServerKey;

        try {
            FileInputStream serviceAccount = new FileInputStream(pathToServiceAccountKey);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
