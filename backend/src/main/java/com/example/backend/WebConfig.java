// Define the package for the web configuration
package com.example.backend;

// Import required Spring configuration classes
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuration class for web-related settings
@Configuration
public class WebConfig {

    // Bean definition for CORS configuration
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configure CORS for all endpoints
                registry.addMapping("/**")
                        // Allow requests from React development server
                        .allowedOrigins("http://localhost:5173")
                        // Allow standard HTTP methods
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Allow all headers
                        .allowedHeaders("*");
            }
        };
    }
}