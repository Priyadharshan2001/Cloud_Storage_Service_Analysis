// Define the package for the Dropbox web scraper class
package com.example.backend.scrapper;

// Import required Selenium classes for web automation
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

// Import Java IO classes for file operations
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Import Java collections class
import java.util.List;

// Main class for Dropbox pricing scraper
public class Dropbox {

    // Main method to start the scraper
    public static void main(String[] args) {
        // Create instance of Dropbox scraper
        Dropbox scraper = new Dropbox();
        // Initialize scraper with output directory
        scraper.init("./temp");
    }

    // Method to initialize the web scraper
    public void init(String directory) {
        // Initialize Chrome WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Define output file path
            String filePath = directory + "/dropbox_plans.csv";
            // Create File object
            File file = new File(filePath);

            // Create parent directories if they don't exist
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            // Initialize FileWriter for CSV output
            FileWriter csvWriter = new FileWriter(filePath);
            // Write CSV header row
            csvWriter.append(
                    "Provider,Plan Name,Price per annum,Price per month,Capacity,File types supported,Special features,Platform compatibility,URL,Contact Email,Contact Number\n");

            // Navigate to yearly pricing page
            driver.get("https://www.dropbox.com/plans?billing=yearly");
            // Find all plan name elements
            List<WebElement> yearlyPlans = driver.findElements(By.cssSelector("[data-testid='plan_name_test_id']"));
            // Find all price elements
            List<WebElement> yearlyPrices = driver.findElements(By.cssSelector("[data-testid='price_test_id']"));

            // Initialize arrays to store plan data
            String[] planNames = new String[yearlyPlans.size()];
            String[] yearlyPricesArray = new String[yearlyPlans.size()];
            // Extract and store yearly plan data
            for (int i = 0; i < yearlyPlans.size(); i++) {
                // Get plan name text
                planNames[i] = yearlyPlans.get(i).getText();
                // Extract numeric price value
                yearlyPricesArray[i] = yearlyPrices.get(i).getText().replaceAll("[^0-9.]", "");
            }

            // Navigate to monthly pricing page
            driver.get("https://www.dropbox.com/plans?billing=monthly");
            // Find all monthly price elements
            List<WebElement> monthlyPrices = driver.findElements(By.cssSelector("[data-testid='price_test_id']"));

            // Initialize array for monthly prices
            String[] monthlyPricesArray = new String[monthlyPrices.size()];
            // Extract and store monthly prices
            for (int i = 0; i < monthlyPrices.size(); i++) {
                // Extract numeric price value
                monthlyPricesArray[i] = monthlyPrices.get(i).getText().replaceAll("[^0-9.]", "");
            }

            // Define XPaths for capacity elements
            String[] capacityXPaths = {
                    "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[1]/div/div/div/div[3]/div/ul/li[2]/span[2]",
                    "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[2]/div/div/div/div[4]/div/ul/li[2]/span[2]",
                    "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[3]/div/div/div[2]/div[4]/div/ul/li[2]/span[2]/span",
                    "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[4]/div/div/div/div[4]/div/ul/li[2]/span[2]/span"
            };

            // Initialize array for capacities
            String[] capacities = new String[capacityXPaths.length];
            // Extract and process capacity data
            for (int i = 0; i < capacityXPaths.length; i++) {
                try {
                    // Find capacity element
                    WebElement capacityElement = driver.findElement(By.xpath(capacityXPaths[i]));
                    // Extract numeric capacity value
                    String rawText = capacityElement.getText();
                    // Format capacity with TB unit
                    String capacityValue = rawText.replaceAll("[^0-9]", "") + " TB";
                    // Store capacity value
                    capacities[i] = capacityValue;
                    // Log capacity value
                    System.out.println("Row " + (i + 1) + " Capacity: " + capacityValue);
                } catch (Exception e) {
                    // Log capacity extraction error
                    System.err.println("Capacity not found for row " + (i + 1) + ": " + e.getMessage());
                    // Set default value if capacity not found
                    capacities[i] = "null";
                }
            }

            // Define XPaths for special features
            String[][] specialFeaturesXPaths = {
                    { "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[1]/div/div/div/div[3]/div/ul/li[4]/span[2]",
                            "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[1]/div/div/div/div[3]/div/ul/li[5]/span[2]" },
                    { "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[2]/div/div/div/div[4]/div/ul/li[4]/span[2]",
                            "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[2]/div/div/div/div[4]/div/ul/li[5]/span[2]" },
                    { "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[3]/div/div/div[2]/div[4]/div/ul/li[4]/span[2]",
                            "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[3]/div/div/div[2]/div[4]/div/ul/li[5]/span[2]" },
                    { "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[4]/div/div/div/div[4]/div/ul/li[4]/span[2]",
                            "/html/body/div[1]/div/div/main/div[2]/div/div/div/div[4]/div/div/div/div[4]/div/ul/li[5]/span[2]" }
            };

            // Initialize array for special features
            String[] specialFeatures = new String[specialFeaturesXPaths.length];
            // Extract and process special features
            for (int i = 0; i < specialFeaturesXPaths.length; i++) {
                try {
                    // Find feature elements
                    WebElement feature1 = driver.findElement(By.xpath(specialFeaturesXPaths[i][0]));
                    WebElement feature2 = driver.findElement(By.xpath(specialFeaturesXPaths[i][1]));
                    // Get feature texts
                    String feature1Text = feature1.getText();
                    String feature2Text = feature2.getText();
                    // Format features with bullet points
                    specialFeatures[i] = "\"" + "- " + feature1Text + "\n- " + feature2Text + "\"";
                    // Log special features
                    System.out.println("Row " + (i + 1) + " Special Features: " + specialFeatures[i]);
                } catch (Exception e) {
                    // Log feature extraction error
                    System.err.println("Special Features not found for row " + (i + 1) + ": " + e.getMessage());
                    // Set default value if features not found
                    specialFeatures[i] = "null";
                }
            }

            // Define URL for all plans
            String url = "https://www.dropbox.com/plans?billing=monthly";

            // Write all data to CSV file
            for (int i = 0; i < planNames.length; i++) {
                csvWriter.append(String.format(
                        "Dropbox,%s,%s,%s,%s,,%s,,%s,,\n",
                        planNames[i],
                        yearlyPricesArray[i],
                        (i < monthlyPricesArray.length) ? monthlyPricesArray[i] : "null",
                        (i < capacities.length) ? capacities[i] : "null",
                        specialFeatures[i],
                        url
                ));
            }

            // Flush and close writer
            csvWriter.flush();
            csvWriter.close();
            // Log success message
            System.out.println("CSV file 'dropbox_plans.csv' has been created successfully!");

        } catch (IOException e) {
            // Log file writing error
            System.out.println("Error writing to CSV file: " + e.getMessage());
        } catch (Exception e) {
            // Log general error
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Quit driver to close browser
            driver.quit();
        }
    }
}