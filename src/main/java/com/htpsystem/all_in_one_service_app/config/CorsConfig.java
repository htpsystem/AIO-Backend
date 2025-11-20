package com.htpsystem.all_in_one_service_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")   // Allow all origins (you can restrict later)
                        .allowedMethods("*")   // GET, POST, PUT, DELETE
                        .allowedHeaders("*");  // Allow headers like Authorization
            }
        };
    }
}
