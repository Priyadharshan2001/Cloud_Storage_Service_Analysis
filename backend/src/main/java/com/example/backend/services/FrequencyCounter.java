// Define the package for the frequency counter service
package com.example.backend.services;

// Import required Java classes
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

// Import utility class for file operations
import com.example.backend.utils.FileUtils;

// Class for counting word frequencies in files
public class FrequencyCounter {

    // Method to read words from a file and return as list
    private List<String> readWordsFromFile(String filePath) {
        // Initialize list to store words
        List<String> words = new ArrayList<>();
        // Try-with-resources to read file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read file line by line
            while ((line = reader.readLine()) != null) {
                // Split line into words using regex
                String[] splitWords = line.toLowerCase().split("[^a-zA-Z0-9]+");
                // Filter out empty strings and add to list
                words.addAll(Arrays.stream(splitWords)
                        .filter(word -> !word.isEmpty())
                        .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            // Print error if file reading fails
            System.err.println("Failed to read file: " + filePath + ": " + e.getMessage());
        }
        return words;
    }

    // Method to build frequency map from word list
    private Map<String, Integer> buildFrequencyMap(List<String> wordList) {
        // Group words and count occurrences
        return wordList.stream()
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.summingInt(word -> 1)));
    }

    // Method to sort frequency map entries by count in descending order
    private List<Entry<String, Integer>> getSortedFrequencies(Map<String, Integer> wordFrequency) {
        return wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    // Method to print top N frequent words
    private void printTopFrequencies(List<Entry<String, Integer>> sortedEntries, int count) {
        // Print header
        System.out.println("Top " + count + " frequently used words:");
        // Print top N entries
        sortedEntries.stream()
                .limit(count)
                .forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
    }

    // Method to get frequency list for a single file
    public List<Entry<String, Integer>> getList(String path) {
        // Read words from file
        List<String> wordList = readWordsFromFile(path);
        // Build frequency map
        Map<String, Integer> frequencyMap = buildFrequencyMap(wordList);
        // Return sorted frequencies
        return getSortedFrequencies(frequencyMap);
    }

    // Method to initialize frequency analysis for all files in directory
    public List<Entry<String, Integer>> init(String directoryPath) {
        // Initialize list to aggregate results
        List<Entry<String, Integer>> aggregatedList = new ArrayList<>();

        // Process all files in directory
        FileUtils.readFiles(directoryPath, path -> {
            // Get frequencies for each file
            List<Entry<String, Integer>> fileList = getList(path.toString());
            // Add to aggregated list
            aggregatedList.addAll(fileList);
        });

        // Sort and return aggregated frequencies
        return getSortedFrequencies(
                aggregatedList.stream()
                        .collect(Collectors.groupingBy(
                                Entry::getKey,
                                Collectors.summingInt(Entry::getValue))));
    }

    // Main method to run frequency analysis
    public static void main(String[] args) {
        // Set directory path and number of top words to display
        String directoryPath = "./backend/data";
        int topN = 10;

        // Create frequency counter instance
        FrequencyCounter sf = new FrequencyCounter();
        // Get aggregated frequency results
        List<Entry<String, Integer>> result = sf.init(directoryPath);

        // Print top N frequent words
        sf.printTopFrequencies(result, topN);
    }
}