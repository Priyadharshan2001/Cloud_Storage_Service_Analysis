// Define the package for the ICloud web scraper class
package com.example.backend.scrapper;

// Import Java IO classes for file operations
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Import Java collections class
import java.util.List;

// Import Selenium classes for web automation
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

// Main class for ICloud cloud storage pricing scraper
public class ICloud {

    // Main method to start the scraper
    public static void main(String[] args) {
        // Create instance of ICloud scraper
        ICloud scraper = new ICloud();
        try {
            // Initialize scraper with output directory
            scraper.init("./temp");
        } catch (Exception e) {
            // Print stack trace if initialization fails
            e.printStackTrace();
        }
    }

    // Method to initialize the web scraper
    public void init(String directory) {
        // Initialize Chrome WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Define output file path
            String path = directory + "/ICloud_plans.csv";
            // Create File object for output
            File file = new File(path);
            // Create parent directories if they don't exist
            file.getParentFile().mkdirs();

            // Initialize FileWriter for CSV output
            FileWriter csvWriter = new FileWriter(path);
            // Write CSV header row
            csvWriter.append(
                "Provider,Plan Name,Price per annum,Price per month,Capacity,File types supported,Special features,Platform compatibility,URL,Contact Email,Contact Number\n");

            // Navigate to ICloud cloud storage pricing page
            driver.get("https://www.apple.com/ca/icloud/#compare-plans");

            // Find and click store navigation link
            WebElement storeLink = driver
                    .findElement(By.xpath("//*[@id='globalnav-list']/li[2]/div/div/div[1]/ul/li[1]/a"));
            storeLink.click();
            // Wait for page to load
            Thread.sleep(3000);

            // Find and click product image
            WebElement imageElement = driver.findElement(
                    By.xpath("//*[@id='shelf-1_section']/div/div[1]/div/div/div[2]/div/div/div/div[1]/img"));
            imageElement.click();
            // Wait for page to load
            Thread.sleep(3000);

            // Find and click buy button
            WebElement buyButton = driver.findElement(
                    By.xpath("//*[@id='shelf-1_section']/div[2]/div[1]/div/div/div[1]/div/div/div/div[3]/div/a"));
            buyButton.click();
            // Wait for page to load
            Thread.sleep(3000);

            // Navigate back to previous pages
            driver.navigate().back();
            Thread.sleep(3000);
            driver.navigate().back();
            Thread.sleep(3000);
            driver.navigate().back();
            Thread.sleep(3000);

            // Initialize JavaScript executor for scrolling
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Find element to scroll to
            WebElement scrollToElement = driver.findElement(By.className("hero-compare-grid-item"));
            // Scroll to the plans section
            js.executeScript("arguments[0].scrollIntoView(true);", scrollToElement);
            // Wait for scroll to complete
            Thread.sleep(2000);

            // Find all plan elements
            List<WebElement> plans = driver.findElements(By.className("hero-compare-grid-item"));
            // Process each plan
            for (WebElement plan : plans) {
                // Extract plan name
                String planName = plan.findElement(By.className("typography-caption")).getText();
                // Extract and clean monthly price
                String pricePerMonth = plan.findElement(By.className("hero-compare-price")).getText()
                        .replaceAll("[^0-9.]", "");
                // Extract capacity
                String capacity = plan.findElement(By.className("hero-compare-plan")).getText();
                // Extract and format special features
                String specialFeatures = "\"" + plan.findElement(By.className("hero-compare-copy")).getText()
                        .replaceAll("\n", " ")
                        .trim() + "\"";
                // Extract plan URL
                String planURL = plan.findElement(By.tagName("a")).getAttribute("href");

                // Write plan data to CSV
                csvWriter.append(String.format(
                        "ICloud,%s,,%s,%s,,%s,,%s,,\n",
                        planName, pricePerMonth, capacity, specialFeatures, planURL));
            }

            // Flush and close writer
            csvWriter.flush();
            csvWriter.close();
            // Log success message
            System.out.println("CSV file 'ICloud_plans.csv' has been created successfully!");

        } catch (IOException e) {
            // Log file writing error
            System.out.println("Error writing to CSV file: " + e.getMessage());
        } catch (InterruptedException e) {
            // Log general error
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Quit driver to close browser
            driver.quit();
        }
    }
}