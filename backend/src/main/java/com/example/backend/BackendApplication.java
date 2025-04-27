// Define the package for the backend application
package com.example.backend;

// Import required Spring Boot classes
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main application class annotated as a Spring Boot application
@SpringBootApplication
public class BackendApplication {

    // Main method that serves as the entry point of the application
    public static void main(String[] args) {
        // Run the Spring Boot application
        SpringApplication.run(BackendApplication.class, args);
    }
}