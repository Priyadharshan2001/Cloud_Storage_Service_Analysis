// Define the package for the search frequency service
package com.example.backend.services;

// Import required Java classes
import java.io.*;//file handling
import java.util.*;//provides data structure List,map,treemap
import java.util.Map.Entry;
import java.util.regex.Pattern;//Helps extract words from text using regular expressions
import java.util.stream.Collectors;
import java.util.stream.Stream;//used storing and processing search term techniques.

// Class representing a node in the AVL tree
class TreeNode {
    // The search term stored in this node
    String term;
    // Frequency count of the search term
    int count;
    // Height of the node in the AVL tree
    int height;
    // Left and right child nodes
    TreeNode left, right;
    //References to child nodes

    // Constructor to create a new tree node
    TreeNode(String term) {
        this.term = term;
        this.count = 1;
        this.height = 1;
    }
}

// Class implementing an AVL tree for search term tracking
class AVLTree {
    // Root node of the tree
    private TreeNode root;
    // Map to log search term frequencies
    private Map<String, Integer> searchLog = new TreeMap<>();

    // Method to get height of a node
    private int nodeHeight(TreeNode node) {
        return node == null ? 0 : node.height;
    }

    // Method to perform right rotation
    private TreeNode rotateRight(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(nodeHeight(y.left), nodeHeight(y.right)) + 1;
        x.height = Math.max(nodeHeight(x.left), nodeHeight(x.right)) + 1;

        return x;
    }

    // Method to perform left rotation
    private TreeNode rotateLeft(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(nodeHeight(x.left), nodeHeight(x.right)) + 1;
        y.height = Math.max(nodeHeight(y.left), nodeHeight(y.right)) + 1;

        return y;
    }

    // Method to calculate balance factor
    private int balanceFactor(TreeNode node) {
        return node == null ? 0 : nodeHeight(node.left) - nodeHeight(node.right);
    }

    // Public method to insert a term
    public void insert(String term) {
        root = addNode(root, term);
    }

    // Recursive method to add a node
    private TreeNode addNode(TreeNode node, String term) {
        if (node == null)
            return new TreeNode(term);

        if (term.equals(node.term)) {
            node.count++;
            return node;
        } else if (term.compareTo(node.term) < 0) {
            node.left = addNode(node.left, term);
        } else {
            node.right = addNode(node.right, term);
        }

        node.height = 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right));

        int balance = balanceFactor(node);

        // Handle rotation cases
        if (balance > 1 && term.compareTo(node.left.term) < 0)
            return rotateRight(node);

        if (balance < -1 && term.compareTo(node.right.term) > 0)
            return rotateLeft(node);

        if (balance > 1 && term.compareTo(node.left.term) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && term.compareTo(node.right.term) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Method to find a term's frequency
    public int findTerm(String term) {
        TreeNode node = findNode(root, term);

        if (node == null) {
            System.out.println("Word: " + term + " not found!");
        } else {
            System.out.println("Found word: " + term + ", Frequency: " + node.count);
        }

        return node == null ? 0 : node.count;
    }

    // Recursive method to find a node
    private TreeNode findNode(TreeNode node, String term) {
        if (node == null || node.term.equals(term))
            return node;

        if (term.compareTo(node.term) < 0)
            return findNode(node.left, term);

        return findNode(node.right, term);
    }

    // Method to show top searches
    public List<Map.Entry<String, Integer>> showTopSearches(int limit) {
        Map<String, Integer> frequencyMap = new TreeMap<>();
        collectFrequencies(root, frequencyMap);

        Stream<Map.Entry<String, Integer>> sortedStream = frequencyMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Map.Entry<String, Integer>> topSearches = (limit > 0 ? sortedStream.limit(limit) : sortedStream)
                .collect(Collectors.toList());

        System.out.println("\nMost Searched Terms:");
        topSearches
                .forEach(entry -> System.out.println("Search: " + entry.getKey() + ", Frequency: " + entry.getValue()));

        return topSearches;
    }

    // Method to collect frequencies from tree
    private void collectFrequencies(TreeNode node, Map<String, Integer> frequencyMap) {
        if (node == null)
            return;

        collectFrequencies(node.left, frequencyMap);
        frequencyMap.put(node.term, node.count);
        collectFrequencies(node.right, frequencyMap);
    }
}

// Main class for search frequency tracking
public class SearchFrequency {
    // AVL tree instance for storing search terms
    AVLTree searchTree = new AVLTree();
    // Directory for storing search history
    String directory = "";
    // File path for search history
    String filePath = directory + "/searchHistory.txt";

    // Constructor
    public SearchFrequency(String directory) {
        this.directory = directory;
        this.filePath = directory + "/searchHistory.txt";
    }

    // Method to initialize search history
    public void init() {
        File file = new File(filePath);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                searchTree.insert(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add search term to history
    public void addHistory(String word) {
        searchTree.insert(word.toLowerCase());

        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            fileWriter.append(word + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get search history
    public List<Map.Entry<String, Integer>> getSearchHistory() {
        return searchTree.showTopSearches(-1);
    }

    // Method to search for a term
    public int searchWord(String term) {
        return searchTree.findTerm(term);
    }

    // Main method
    public static void main(String[] args) {
        AVLTree searchTree = new AVLTree();

        String directory = "./backend/data";
        String filePath = directory + "/searchHistory.txt";

        File file = new File(filePath);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : getWords(line)) {
                    searchTree.insert(word.toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("\nEnter search term (or type 'exit' to stop): ");
            String query = input.nextLine().trim();
            if (query.equalsIgnoreCase("exit")) {
                break;
            }
            searchTree.findTerm(query);
        }
        input.close();

        System.out.println("Top Searches:");
        searchTree.showTopSearches(-1);
    }

    // Method to extract words from text
    private static Set<String> getWords(String text) {
        Set<String> words = new HashSet<>();
        Pattern pattern = Pattern.compile("\\b\\w+\\b");

        var matcher = pattern.matcher(text);
        while (matcher.find()) {
            words.add(matcher.group());
        }

        return words;
    }
}