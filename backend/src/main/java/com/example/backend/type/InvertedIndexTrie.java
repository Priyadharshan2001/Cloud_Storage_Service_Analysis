// Define the package for the inverted index trie implementation
package com.example.backend.type;

// Import required Java collections
import java.util.*;

// Class representing a node in the trie
class TrieNode {
    // Map of child nodes keyed by character
    Map<Character, TrieNode> children = new HashMap<>();
    // Flag indicating end of a word
    boolean isEndOfWord = false;
    // Map storing document positions (document -> line numbers)
    Map<String, HashSet<Integer>> documentPositions = new HashMap<>();
}

// Class implementing an inverted index using a trie
public class InvertedIndexTrie {
    // Root node of the trie
    private TrieNode root = new TrieNode();

    // Method to add a word to the trie
    public void addWord(String word, String document, int lineNumber) {
        TrieNode current = root;
        // Traverse each character in the word
        for (char c : word.toLowerCase().toCharArray()) {
            // Create new node if character doesn't exist
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        // Mark end of word and store document position
        current.isEndOfWord = true;
        current.documentPositions.putIfAbsent(document, new HashSet<>());
        current.documentPositions.get(document).add(lineNumber);
    }

    // Method to search for a query in the trie
    public Map<String, HashSet<Integer>> search(String query) {
        TrieNode current = root;
        // Traverse each character in the query
        for (char c : query.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(c)) {
                System.out.println("No match for query: " + query);
                return new HashMap<>();
            }
            current = current.children.get(c);
        }

        // Return results if query matches a complete word
        if (current.isEndOfWord) {
            return current.documentPositions;
        }

        System.out.println("Query found as prefix but not a complete term: " + query);
        return new HashMap<>();
    }

    // Method to find autocomplete suggestions for a prefix
    public List<String> autocomplete(String prefix) {
        TrieNode current = root;
        // Navigate to the prefix node
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return Collections.emptyList();
            }
            current = current.children.get(c);
        }

        // Collect all words starting with the prefix
        List<String> suggestions = new ArrayList<>();
        collectAllWords(current, prefix, suggestions);
        return suggestions;
    }

    // Helper method to recursively collect all words from a node
    private void collectAllWords(TrieNode node, String prefix, List<String> suggestions) {
        // Add word if current node marks end of a word
        if (node.isEndOfWord) {
            suggestions.add(prefix);
        }
        // Recursively collect words from child nodes
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            collectAllWords(entry.getValue(), prefix + entry.getKey(), suggestions);
        }
    }

    // Method to search for numeric values within a range
    public Map<String, HashSet<Integer>> searchRange(String key, double minValue, double maxValue) {
        // Initialize results map
        Map<String, HashSet<Integer>> results = new HashMap<>();

        TrieNode current = root;
        String searchPrefix = key.toLowerCase() + ":";
        // Navigate to the prefix node
        for (char c : searchPrefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return results;
            }
            current = current.children.get(c);
        }

        // Collect all terms with the given prefix
        List<String> matchedTerms = new ArrayList<>();
        collectAllWords(current, searchPrefix, matchedTerms);

        // Check each matched term against the value range
        for (String term : matchedTerms) {
            String[] parts = term.split(":");
            if (parts.length == 2) {
                try {
                    double termValue = Double.parseDouble(parts[1]);
                    // Add to results if value is within range
                    if (termValue >= minValue && termValue <= maxValue) {
                        Map<String, HashSet<Integer>> termResults = search(term);
                        // Merge results into main results map
                        for (Map.Entry<String, HashSet<Integer>> entry : termResults.entrySet()) {
                            results.merge(entry.getKey(),
                                    new HashSet<>(entry.getValue()),
                                    (oldSet, newSet) -> {
                                        oldSet.addAll(newSet);
                                        return oldSet;
                                    });
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip non-numeric terms
                }
            }
        }
        return results;
    }
}