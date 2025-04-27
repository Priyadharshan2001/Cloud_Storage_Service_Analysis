// Define the package for the search service
package com.example.backend.services;

// Import required classes
import com.example.backend.type.InvertedIndexTrie;
import com.example.backend.utils.FileUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

// Import Java IO and utility classes
import java.io.FileReader;
import java.util.*;

// Class implementing search functionality using inverted index
public class Search {
    // Inverted index for general text search
    public InvertedIndexTrie invertedIndex = new InvertedIndexTrie();
    // Inverted index for key-value pair search
    public InvertedIndexTrie invertedIndexKeyMapped = new InvertedIndexTrie();
    // Map to store CSV headers by file
    static Map<String, String[]> fileHeaders = new HashMap<>();
    // Map to store CSV rows by file
    static Map<String, List<String[]>> fileRows = new HashMap<>();
    // Set to store unique storage sizes
    public HashSet<String> storageSizes = new HashSet<>();

    // Method to build the inverted index trie
    public void buildTrie() {
        System.out.println("Building the inverted index Trie. Please wait...");

        try {
            // Process all files in the data directory
            FileUtils.readFiles("./data", path -> {
                System.out.println("Reading file: " + path.toString());
                // Initialize CSV reader
                try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(path.toString())).build()) {
                    String documentId = path.toString();

                    // Read CSV headers
                    String[] headers = csvReader.readNext();
                    if (headers == null) {
                        System.out.println("File " + documentId + " is empty or missing headers.");
                        return;
                    }
                    fileHeaders.put(documentId, headers);

                    List<String[]> rows = new ArrayList<>();
                    String[] row;
                    int lineNumber = 0;

                    // Process each CSV row
                    while ((row = csvReader.readNext()) != null) {
                        lineNumber++;
                        rows.add(row);

                        // Process each column in the row
                        for (int i = 0; i < row.length; i++) {
                            String key = headers[i].toLowerCase();
                            String value = row[i];

                            if (value != null && !value.isEmpty()) {
                                // Normalize capacity values
                                if (key.equals("capacity")) {
                                    value = normalizeCapacity(value);
                                    storageSizes.add(value);
                                }
                            }

                            if (value != null && !value.isEmpty()) {
                                // Add key-value pair to mapped index
                                String indexedTerm = key + ":" + value.toLowerCase();
                                invertedIndexKeyMapped.addWord(indexedTerm, documentId, lineNumber);

                                // Add value to general index
                                invertedIndex.addWord(row[i].toLowerCase(), documentId, lineNumber);
                            }
                        }
                    }
                    fileRows.put(documentId, rows);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            System.out.println("Inverted index Trie has been built successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to normalize storage capacity values
    private String normalizeCapacity(String value) {
        value = value.trim().toUpperCase();
        // Convert GB to numeric value
        if (value.endsWith("GB")) {
            return value.replace("GB", "").trim();
        }
        // Convert TB to GB (numeric value)
        else if (value.endsWith("TB")) {
            double tbToGb = Double.parseDouble(value.replace("TB", "").trim()) * 1024;
            return String.valueOf((int) tbToGb);
        }
        return value;
    }

    // Main method to run search interface
    public static void main(String[] args) {
        try {
            Search search = new Search();
            // Build the search index
            search.buildTrie();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nEnter a word to search or prefix for suggestions (type 'exit' to quit): ");
                String query = scanner.nextLine().trim().toLowerCase();

                // Exit condition
                if (query.equals("exit")) {
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                }

                // Provide autocomplete suggestions
                List<String> suggestions = search.invertedIndex.autocomplete(query);
                if (!suggestions.isEmpty()) {
                    System.out.println("Autocomplete suggestions for '" + query + "': " +
                            suggestions);
                }

                // Perform search
                Map<String, HashSet<Integer>> searchResults = search.invertedIndex.search(query);
                if (searchResults.isEmpty()) {
                    System.out.println("No results found for '" + query + "'.");
                } else {
                    System.out.println("Result '" + searchResults + "'.");

                    // Convert results to JSON format
                    search.convertToJson(searchResults);
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to convert search results to JSON format
    public List<Map<String, Object>> convertToJson(Map<String, HashSet<Integer>> searchResults) {
        List<Map<String, Object>> outputList = new ArrayList<>();

        // Process each document in search results
        searchResults.forEach((documentId, lineNumbers) -> {
            String[] headers = fileHeaders.get(documentId);
            List<String[]> rows = fileRows.get(documentId);

            // Process each matching line number
            for (int lineNumber : lineNumbers) {
                if (lineNumber - 1 < rows.size()) {
                    Map<String, Object> rowMap = new HashMap<>();
                    // Create unique ID for result
                    rowMap.put("id", lineNumber + "_" + documentId);
                    rowMap.put("document", documentId);
                    String[] row = rows.get(lineNumber - 1);

                    // Add all columns to the result
                    for (int i = 0; i < headers.length && i < row.length; i++) {
                        rowMap.put(headers[i], row[i]);
                    }

                    outputList.add(rowMap);
                }
            }
        });

        return outputList;
    }
}