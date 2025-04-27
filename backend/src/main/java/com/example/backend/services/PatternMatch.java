package com.example.backend.services;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.Period;

public class PatternMatch {
    // Regex pattern for validating email addresses
    final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    
    // Regex pattern for validating names (letters and spaces only, 2-50 chars)
    final String nameRegex = "^[a-zA-Z\\s]{2,50}$";
    
    // Regex pattern for US phone numbers (+1 followed by 10 digits)
    final String phoneRegex = "^\\+1\\d{10}$";
    
    // Regex pattern for password (at least 8 chars, 1 uppercase, 1 lowercase, 1 number)
    final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

    // Method to check if a string matches the email pattern
    public boolean emailCheck(String text) {
        System.out.println("Validating email: " + text);
        return Pattern.compile(emailRegex).matcher(text).matches();
    }

    // Method to validate full name (letters and spaces only)
    public boolean nameCheck(String name) {
        System.out.println("Validating name: " + name);
        return Pattern.compile(nameRegex).matcher(name).matches();
    }

    // Method to validate US phone number (+1XXXXXXXXXX format)
    public boolean phoneCheck(String phone) {
        System.out.println("Validating phone: " + phone);
        return Pattern.compile(phoneRegex).matcher(phone).matches();
    }

    // Method to validate password strength
    public boolean passwordCheck(String password) {
        System.out.println("Validating password");
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    // Method to validate date of birth (must be at least 18 years old)
    public boolean dobCheck(String dobString) {
        System.out.println("Validating DOB: " + dobString);
        try {
            LocalDate dob = LocalDate.parse(dobString);
            LocalDate now = LocalDate.now();
            int age = Period.between(dob, now).getYears();
            return age >= 18;
        } catch (Exception e) {
            return false; // Invalid date format
        }
    }

    // Comprehensive validation method that returns specific error messages
    public String validateAll(String name, String dob, String phone, String email, String password) {
        if (!nameCheck(name)) {
            return "Invalid name. Only letters and spaces allowed (2-50 characters).";
        }
        if (!dobCheck(dob)) {
            return "Invalid date of birth. You must be at least 18 years old.";
        }
        if (!phoneCheck(phone)) {
            return "Invalid phone number. Must be in +1XXXXXXXXXX format.";
        }
        if (!emailCheck(email)) {
            return "Invalid email address format.";
        }
        if (!passwordCheck(password)) {
            return "Password must be at least 8 characters with 1 uppercase, 1 lowercase, and 1 number.";
        }
        return null; // null indicates no errors
    }
}