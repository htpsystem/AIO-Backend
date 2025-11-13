package com.htpsystem.all_in_one_service_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())                // Disable CSRF (important for Postman)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register").permitAll()  // PUBLIC
                        .anyRequest().permitAll()             // Make entire app public for now
                )
                .formLogin(form -> form.disable())            // Disable Spring Login Page
                .httpBasic(basic -> basic.disable());         // Disable Browser Auth popup

        System.out.println("🔥 SecurityConfig ACTIVE → all routes open!");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
