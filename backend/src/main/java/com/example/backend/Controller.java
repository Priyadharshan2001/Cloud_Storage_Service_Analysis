// Define the package for the backend controller
package com.example.backend;

// Import required Java and Spring classes
import java.util.*;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
// Import model classes
import com.example.backend.model.*;
// Import service classes
import com.example.backend.services.*;

// Spring REST Controller class
@RestController
public class Controller {
    // Service instances
    private static final Search search = new Search();
    private static final SpellCheck spellCheck = new SpellCheck();
    private static final SearchFrequency searchFrequency = new SearchFrequency("./data");
    private static final FrequencyCounter frequencyCounter = new FrequencyCounter();
    private static final PatternMatch patternMatch = new PatternMatch();
    private static final DataValidation dv = new DataValidation();

    // Initialize services after construction
    @PostConstruct
    public void init() {
        search.buildTrie();
        spellCheck.buildDictionary();
        searchFrequency.init();
        dv.init("./data");
    }

    // Endpoint for autocomplete suggestions
    @GetMapping("/auto_complete")
    public AutoComplete autoComplete(@RequestParam(value = "q", defaultValue = "") String query) {
        List<String> list = search.invertedIndex.autocomplete(query);
        System.out.println(list);
        return new AutoComplete(list);
    }

    // Endpoint for storage size options
    @GetMapping("/storage_list")
    public StorageList storageList(@RequestParam(value = "q", defaultValue = "") String query) {
        List<Object> list = Arrays.asList(search.storageSizes.toArray());
        System.out.println(list);
        return new StorageList(list);
    }

    // Endpoint for search history
    @GetMapping("/search_history")
    public SearchHistory searchHistory() {
        return new SearchHistory(searchFrequency.getSearchHistory());
    }

    // Endpoint for search term frequency
    @GetMapping("/search_history_term_freq")
    public SearchTermFrequency searchHistoryTermFrequency(@RequestParam(value = "q", defaultValue = "") String term) {
        return new SearchTermFrequency(searchFrequency.searchWord(term));
    }

    // Endpoint for word frequency analysis
    @GetMapping("/frequency_counter")
    public WordFrequency wordFrequency() {
        return new WordFrequency(frequencyCounter.init("./data"));
    }

    // Endpoint for email validation
    @GetMapping("/subscribe_to_newsletter")
    public PatternMatchModel subscribe(@RequestParam(value = "email", defaultValue = "") String email) {
        return new PatternMatchModel(patternMatch.emailCheck(email));
    }

    // Endpoint for data validation results
    @GetMapping("/validate_data")
    public ValidateData validateData() {
        System.out.println("Data");
        dv.printInvalidLines();
        return new ValidateData(dv.invalidLinesByFile);
    }

    // Main search endpoint with multiple filters
    @GetMapping("/search")
    public SearchQuery search(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "minPrice", defaultValue = "") String minPrice,
            @RequestParam(value = "maxPrice", defaultValue = "") String maxPrice,
            @RequestParam(value = "minStorage", defaultValue = "") String minStorage,
            @RequestParam(value = "maxStorage", defaultValue = "") String maxStorage) {

        // Parse numeric parameters with defaults
        double minPriceVal = parseDoubleWithDefault(minPrice, 0);
        double maxPriceVal = parseDoubleWithDefault(maxPrice, Double.MAX_VALUE);
        double minStorageVal = parseDoubleWithDefault(minStorage, 0);
        double maxStorageVal = parseDoubleWithDefault(maxStorage, Double.MAX_VALUE);

        // Initialize search results
        Map<String, HashSet<Integer>> searchResultIndex = new HashMap<>();
        String string = "";

        // Process text query
        if (!query.isEmpty()) {
            searchResultIndex = search.invertedIndex.search(query);
            searchFrequency.addHistory(query);
            System.out.println(searchResultIndex.entrySet().isEmpty());
            if (searchResultIndex.entrySet().isEmpty()) {
                string = spellCheck.findClosestWord(query);
            }
        }

        // Process price range filter
        if (minPriceVal > 0 || maxPriceVal < Double.MAX_VALUE) {
            Map<String, HashSet<Integer>> priceResults = search.invertedIndexKeyMapped.searchRange(
                    "price per month", minPriceVal, maxPriceVal);
            mergeResults(searchResultIndex, priceResults);
        }

        // Process storage range filter
        if (minStorageVal > 0 || maxStorageVal < Double.MAX_VALUE) {
            Map<String, HashSet<Integer>> storageResults = search.invertedIndexKeyMapped.searchRange(
                    "capacity", minStorageVal, maxStorageVal);
            mergeResults(searchResultIndex, storageResults);
        }

        // Convert results to JSON format
        List<Map<String, Object>> list = search.convertToJson(searchResultIndex);

        return new SearchQuery(list, string);
    }

    // Helper method to safely parse double values
    private double parseDoubleWithDefault(String value, double defaultValue) {
        try {
            if (value == null || value.isEmpty()) {
                return defaultValue;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for value: " + value + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    // Helper method to merge search result maps
    private void mergeResults(Map<String, HashSet<Integer>> mainResults,
            Map<String, HashSet<Integer>> newResults) {
        newResults.forEach((key, value) -> mainResults.merge(key, value, (oldSet, newSet) -> {
            oldSet.addAll(newSet);
            return oldSet;
        }));
    }
}