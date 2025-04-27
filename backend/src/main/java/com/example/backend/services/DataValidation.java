// Define the package for the data validation service
package com.example.backend.services;

// Import required Java classes
import java.io.*;
import java.util.*;
import java.util.regex.*;
// Import utility class for file operations
import com.example.backend.utils.FileUtils;
// Import Java NIO classes for file operations
import java.nio.file.*;

// Class for validating data in CSV files
public class DataValidation {

    // Regex pattern for validating URLs
    private final String URL_PATTERN = "^https?://[\\w.-]+(/\\S*)?$";
    // Regex pattern for validating storage capacity
    private final String CAPACITY_PATTERN = "^\\d+\\s?(GB|TB)$";
    // Regex pattern for validating monthly prices
    private final String PRICE_PER_MONTH_PATTERN = "^(0|[1-9]\\d*)(\\.\\d{2})?$";
    // Regex pattern for validating annual prices
    private final String PRICE_PER_ANNUM_PATTERN = "^(0|[1-9]\\d*)(\\.\\d{2})?$";

    // Map to store invalid lines grouped by filename
    public Map<String, List<String>> invalidLinesByFile = new HashMap<>();

    // Main method to start validation
    public static void main(String[] args) {
        // Create instance of DataValidation
        DataValidation dv = new DataValidation();
        // Initialize validation with data directory
        dv.init("./backend/data");
    }

    // Method to initialize validation process
    public void init(String directory) {
        // Print initialization message
        System.out.println("Init Data Validation");

        // Clear previous validation results
        invalidLinesByFile.clear();
        // Read and validate all files in directory
        FileUtils.readFiles(directory, path -> {
            validate(path.toString());
        });
    }

    // Method to validate a single file
    public void validate(String filePath) {
        // Print current file being validated
        System.out.println("Validating path: " + filePath);
        // Extract filename from path
        String fileName = Paths.get(filePath).getFileName().toString();
        // List to store invalid lines for current file
        List<String> fileInvalidLines = new ArrayList<>();

        // Try-with-resources to read file
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            // Read and skip header line
            String header = br.readLine();
            // Check if file is empty
            if (header == null) {
                System.out.println("The file is empty.");
                return;
            }

            // Initialize line counter (starting after header)
            int lineNumber = 1;
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Split line into fields
                String[] fields = line.split(",", -1);
                // Check if line has enough fields
                if (fields.length < 8) {
                    fileInvalidLines.add("Line " + lineNumber + ": Insufficient fields - " + line);
                    continue;
                }

                // Extract and clean specific fields
                String url = fields[7].trim();
                String capacity = fields[4].trim().replaceAll("\\s+", " ");
                String pricePerMonth = fields[2].trim().replaceAll("\\$", "");
                String pricePerAnnum = fields[3].trim().replaceAll("\\$", "");

                // Validate all required fields
                boolean isValid = validateField(url, URL_PATTERN) &&
                        validateField(capacity, CAPACITY_PATTERN) &&
                        validateField(pricePerMonth, PRICE_PER_MONTH_PATTERN) &&
                        validateField(pricePerAnnum, PRICE_PER_ANNUM_PATTERN);

                // Add invalid lines to list
                if (!isValid) {
                    fileInvalidLines.add("Line " + lineNumber + ": " + line);
                }
            }

            // Store invalid lines for this file if any found
            if (!fileInvalidLines.isEmpty()) {
                invalidLinesByFile.put(fileName, fileInvalidLines);
            }

        } catch (IOException e) {
            // Print error if file reading fails
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // Method to print all invalid lines found
    public void printInvalidLines() {
        System.out.println("\n=== Invalid Lines by File ===");
        // Check if any invalid lines were found
        if (invalidLinesByFile.isEmpty()) {
            System.out.println("No invalid lines found.");
            return;
        }

        // Print invalid lines grouped by filename
        for (Map.Entry<String, List<String>> entry : invalidLinesByFile.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (String invalidLine : entry.getValue()) {
                System.out.println("  " + invalidLine);
            }
            System.out.println(); // Empty line between files
        }
    }

    // Helper method to validate a field against a regex pattern
    private static boolean validateField(String field, String pattern) {
        return Pattern.matches(pattern, field);
    }
}