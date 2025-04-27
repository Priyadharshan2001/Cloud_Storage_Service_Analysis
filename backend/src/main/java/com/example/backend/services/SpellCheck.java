// Define the package for the spell check service
package com.example.backend.services;

// Import required utility classes
import com.example.backend.utils.FileUtils;
// Import CSV reader classes
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

// Import Java IO and utility classes
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Class implementing spell checking functionality
public class SpellCheck {

    // List to store dictionary words
    private final List<String> dictionary = new ArrayList<>();

    // Method to compute edit distance between two words
    public static int computeEditDistance(String word1, String word2) {
        // Get lengths of both words
        int len1 = word1.length();
        int len2 = word2.length();
        // Initialize DP table
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Fill DP table
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                // Base cases
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Minimum of insert, delete, or replace operations
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[len1][len2];
    }

    // Method to build dictionary from CSV files
    public void buildDictionary() {
        // Directory containing CSV files
        String directoryPath = "./data";
        System.out.println("Building the dictionary from CSV files...");

        try {
            // Read all files in directory
            FileUtils.readFiles(directoryPath, path -> {
                try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(path.toString())).build()) {
                    String[] row;
                    // Read each row in CSV
                    while ((row = csvReader.readNext()) != null) {
                        // Process each cell in row
                        for (String cell : row) {
                            // Split cell content into words
                            String[] words = cell.toLowerCase().split("\\W+");
                            // Add unique words to dictionary
                            for (String word : words) {
                                if (!word.isEmpty() && !dictionary.contains(word)) {
                                    dictionary.add(word);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Dictionary built with " + dictionary.size() + " unique words.");
    }

    // Method to find closest matching word
    public String findClosestWord(String misspelledWord) {
        String closestWord = null;
        int minDistance = Integer.MAX_VALUE;

        // Compare against all words in dictionary
        for (String word : dictionary) {
            // Compute edit distance
            int distance = computeEditDistance(misspelledWord.toLowerCase(), word);
            // Update closest match if needed
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = word;
            }
        }

        System.out.println("Closest match: \"" + closestWord + "\", Edit Distance: " + minDistance);

        return closestWord;
    }

    // Main method to test functionality
    public static void main(String[] args) {
        // Create spell checker instance
        SpellCheck autoCorrection = new SpellCheck();
        // Build dictionary
        autoCorrection.buildDictionary();

        // Get user input
        System.out.print("Enter a misspelled word: ");
        try (Scanner scanner = new Scanner(System.in)) {
            String misspelledWord = scanner.nextLine();
            // Find and print closest match
            String result = autoCorrection.findClosestWord(misspelledWord);
            System.out.println(result);
        }
    }
}