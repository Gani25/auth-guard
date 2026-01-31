package com.sprk.security_demo_project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Allow Angular app
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // Allow HTTP methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow headers (IMPORTANT for JWT)
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Allow sending token
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
