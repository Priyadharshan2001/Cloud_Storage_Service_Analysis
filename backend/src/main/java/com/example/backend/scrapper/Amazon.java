// Define the package for the Amazon web scraper class
package com.example.backend.scrapper;

// Import required Selenium classes for web automation
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// Import Java IO classes for file operations
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Import Java time class for duration
import java.time.Duration;
// Import Java collections classes
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Main class for Amazon S3 pricing scraper
public class Amazon {

    // Main method to start the scraper
    public static void main(String[] args) {
        // Create instance of Amazon scraper
        Amazon scraper = new Amazon();
        // Initialize scraper with output directory
        scraper.init("./temp");
    }

    // Method to initialize the web scraper
    public void init(String directory) {
        // Initialize Chrome WebDriver
        WebDriver driver = new ChromeDriver();
        // Maximize browser window
        driver.manage().window().maximize();
        // Initialize WebDriverWait with 10 second timeout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Define output file path for CSV
        String path = directory + "/amazon_s3_pricing_table.csv";

        // Create File object for output
        File file = new File(path);
        // Create parent directories if they don't exist
        file.getParentFile().mkdirs();

        // Try-with-resources for FileWriter to ensure proper resource cleanup
        try (FileWriter csvWriter = new FileWriter(path)) {
            // Write CSV header row
            csvWriter.append(
                    "Provider,Plan Name,Price per annum,Price per month,Capacity,File types supported,Special features,Platform compatibility,URL,Contact Email,Contact Number\n");

            // Navigate to Amazon S3 homepage
            driver.get("https://aws.amazon.com/s3/");
            // Set provider name constant
            String providerName = "Amazon S3";

            // Try to close any popup that might appear
            try {
                // Wait for popup close button to be clickable
                WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".popup-close-button")));
                // Click the close button
                closeButton.click();
                // Log success message
                System.out.println("Popup closed successfully.");
            } catch (Exception e) {
                // Log if no popup found
                System.out.println("No popup detected or popup not interactable.");
            }

            // Navigate to pricing page
            System.out.println("Navigating to the Pricing page...");
            // Wait for pricing link to be clickable
            WebElement pricingLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/header/div[3]/div/div/div[2]/a[4]")));
            // Click the pricing link
            pricingLink.click();

            // Find all plan name elements (strong or bold tags)
            List<WebElement> planNameElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath(".//b | .//strong")));

            // Define XPaths for capacity elements
            String[] capacityXpaths = {
                    "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[2]/td[1]",
                    "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[3]/td[1]",
                    "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[4]/td[1]",
                    "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[7]/td[1]",
                    "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[8]/td[1]",
                    "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[9]/td[1]"
            };

            // Create set to track unique plan names
            Set<String> uniquePlanNames = new HashSet<>();

            // Process each plan element
            int rowsToProcess = planNameElements.size() - 1; // Skip last row
            for (int i = 0; i < rowsToProcess; i++) {
                // Handle special case for first row
                String planName;
                if (i == 0) {
                    planName = planNameElements.get(1).getText().trim();
                } else {
                    planName = planNameElements.get(i + 1).getText().trim();
                }

                // Skip if plan name is empty or duplicate
                if (planName.isEmpty() || !uniquePlanNames.add(planName)) {
                    continue;
                }

                // Monthly price is not available
                String monthlyPrice = "";

                // Initialize capacity string
                String capacity = "";
                try {
                    // Find capacity element using XPath
                    WebElement capacityElement = driver.findElement(By.xpath(capacityXpaths[i]));
                    // Get raw capacity text
                    String rawCapacity = capacityElement.getText().trim();

                    // Process capacity if it contains TB
                    if (rawCapacity.contains("TB")) {
                        // Extract numeric part
                        String numericPart = rawCapacity.replaceAll("[^0-9]", "");
                        if (!numericPart.isEmpty()) {
                            capacity = numericPart + " TB";
                        }
                    }
                } catch (Exception e) {
                    // Log capacity extraction error
                    System.out.println("Error extracting capacity for row " + i);
                }

                // Initialize special features
                String specialFeatures = "N/A";
                try {
                    // Handle special features for each row differently
                    if (i == 0) {
                        // First row special features
                        WebElement specialFeatureElement = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[1]/td[1]"));
                        specialFeatures = specialFeatureElement.getText().trim();
                    } else if (i == 1) {
                        // Second row special features (combined)
                        WebElement specialFeaturePart1 = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[2]/td[1]"));
                        WebElement specialFeaturePart2 = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[2]/td[2]"));
                        specialFeatures = specialFeaturePart1.getText().trim() + " : "
                                + specialFeaturePart2.getText().trim();
                    } else if (i == 2) {
                        // Third row special features (combined)
                        WebElement specialFeaturePart1 = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[3]/td[1]"));
                        WebElement specialFeaturePart2 = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[3]/td[2]"));
                        specialFeatures = specialFeaturePart1.getText().trim() + " : "
                                + specialFeaturePart2.getText().trim();
                    } else if (i == 3) {
                        // Fourth row special features (combined)
                        WebElement specialFeaturePart1 = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[3]/td[1]"));
                        WebElement specialFeaturePart2 = driver.findElement(By.xpath(
                                "/html/body/div[2]/main/div[3]/div/div[2]/ul[2]/li[2]/div/div[3]/div/main/div/table/tbody/tr[4]/td[2]"));
                        specialFeatures = specialFeaturePart1.getText().trim() + " : "
                                + specialFeaturePart2.getText().trim();
                    }
                } catch (Exception e) {
                    // Log special features extraction error
                    System.out.println("Error extracting special features for row " + i);
                }

                // Write data to CSV file
                csvWriter.append(
                        String.format("%s,%s,%s,%s,%s,All,\"%s\",ALL,https://aws.amazon.com/s3/?nc=sn&loc=1,,\n",
                                providerName, planName, "", monthlyPrice, capacity, specialFeatures));
            }

            // Flush writer to ensure all data is written
            csvWriter.flush();
            // Log success message
            System.out.println("Data successfully written to amazon_s3_pricing_table.csv");

        } catch (IOException e) {
            // Log file writing error
            System.out.println("Error writing to CSV file: " + e.getMessage());
        } catch (Exception e) {
            // Log general scraping error
            System.out.println("Error during web scraping: " + e.getMessage());
        } finally {
            // Quit driver to close browser
            driver.quit();
        }
    }
}