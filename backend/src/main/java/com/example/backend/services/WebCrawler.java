// Define the package for the web crawler service
package com.example.backend.services;

// Import required Java IO class
import java.io.IOException;

// Import all scraper classes
import com.example.backend.scrapper.Amazon;
import com.example.backend.scrapper.Dropbox;
import com.example.backend.scrapper.GoogleDrive;
import com.example.backend.scrapper.ICloud;
import com.example.backend.scrapper.Azure;

// Main class for coordinating web crawling tasks
public class WebCrawler {

    // Main method to start the crawler
    public static void main(String[] args) {
        // Create WebCrawler instance
        WebCrawler crawl = new WebCrawler();
        // Initialize crawling process
        crawl.init();
    }

    // Method to initialize all web scrapers
    public void init() {
        try {
            // Directory to store scraped data
            String directory = "./data";
            // Initialize and run each scraper
            (new GoogleDrive()).init(directory);
            (new Dropbox()).init(directory);
            (new ICloud()).init(directory);
            (new Azure()).init(directory);
            (new Amazon()).init(directory);
        } catch (IOException e) {
            // Print error if any scraper fails
            e.printStackTrace();
        }
    }
}