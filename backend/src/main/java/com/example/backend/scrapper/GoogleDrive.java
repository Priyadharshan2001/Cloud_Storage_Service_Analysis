// Define the package for the Google Drive web scraper class
package com.example.backend.scrapper;

// Import Java IO classes for file operations
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Import Java time class for duration
import java.time.Duration;
// Import Java collections and regex classes
import java.util.List;
import java.util.regex.Pattern;

// Import Selenium classes for web automation
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
// Import Selenium wait classes
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

// Main class for Google Drive pricing scraper
public class GoogleDrive {

    // Main method to start the scraper
    public static void main(String[] args) {
        // Create instance of GoogleDrive scraper
        GoogleDrive googleDrive = new GoogleDrive();
        try {
            // Initialize scraper with output directory
            googleDrive.init("./temp");
        } catch (IOException e) {
            // Print stack trace if initialization fails
            e.printStackTrace();
        }
    }

    // Method to initialize the web scraper
    public void init(String directory) throws IOException {
        // Initialize Chrome WebDriver
        WebDriver driver = new ChromeDriver();
        try {
            // Navigate to Google Drive pricing page
            driver.get("https://one.google.com/about/plans");

            // Find all pricing card elements
            List<WebElement> pricingList = driver.findElements(By.cssSelector("div.wp6rf > div"));

            // Initialize WebDriverWait with 2 second timeout
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            // Wait until pricing cards are loaded
            wait.until(d -> !pricingList.isEmpty());

            // Define output file path
            String path = directory + "/google-drive.csv";

            // Create File object for output
            File file = new File(path);
            // Create parent directories if they don't exist
            file.getParentFile().mkdirs();

            // Initialize FileWriter for CSV output
            FileWriter pricingFileWriter = new FileWriter(path);

            // Write CSV header row
            pricingFileWriter.append(
                "Provider,Plan Name,Price per annum,Price per month,Capacity,File types supported,Special features,Platform compatibility,URL,Contact Email,Contact Number\n");

            // Process each pricing card
            for (WebElement priceCard : pricingList) {
                // Extract provider name
                String provider = "google";
                // Extract plan name if available
                String planName = !priceCard.findElements(By.cssSelector("div.YgStxe")).isEmpty()
                        ? priceCard.findElement(By.cssSelector("div.YgStxe")).getText()
                        : "";
                // Initialize annual price
                String pricePerAnnum = "";
                // Extract monthly price if available
                String pricePerMonth = !priceCard.findElements(By.cssSelector("div.tKV7vb > span")).isEmpty()
                        ? priceCard.findElement(By.cssSelector("div.tKV7vb > span")).getText()
                        : "";
                // Extract capacity
                String capacity = priceCard.findElement(By.cssSelector("div.Qnu87d.CMqFSd")).getText();
                // Set supported file types
                String fileTypesSupported = "All";
                // Set platform compatibility
                String platformCompatibility = "All";
                // Get current URL
                String url = driver.getCurrentUrl();
                // Initialize contact fields
                String contactEmail = "";
                String contactNumber = "";

                // Build special features string
                StringBuilder specialFeatures = new StringBuilder("\"");

                // Find all feature elements
                List<WebElement> featureList = priceCard.findElements(By.cssSelector("ul.OWqi7c > li"));
                for (WebElement feature : featureList) {
                    // Find feature text element
                    WebElement e = feature.findElement(By.cssSelector("span.ZI49d"));
                    String text = "";
                    // Check if feature has dropdown button
                    if (!e.findElements(By.cssSelector("button > span")).isEmpty()) {
                        WebElement btn = e.findElement(By.cssSelector("button"));
                        text = btn.findElement(By.cssSelector("span")).getText();
                    } else {
                        // Get direct feature text
                        text = e.getText();
                    }
                    // Append formatted feature text
                    specialFeatures.append("- " + text).append("\n");
                }
                specialFeatures.append("\"");

                // Find pricing tabs (monthly/yearly)
                List<WebElement> tabs = driver.findElements(By.cssSelector(
                        "#upgrade > div.k7aPGc > c-wiz > div.S4aDh > div > button"));
                if (!tabs.isEmpty()) {
                    // Click yearly tab
                    tabs.get(1).click();
                    // Extract yearly price if available
                    pricePerAnnum = !priceCard.findElements(By.cssSelector("div.tKV7vb > span")).isEmpty()
                            ? priceCard.findElement(By.cssSelector("div.tKV7vb > span")).getText()
                            : "";
                    // Switch back to monthly tab
                    tabs.get(0).click();
                }

                // Write extracted data to CSV
                pricingFileWriter.append(provider + "," + planName + ","
                        + Pattern.compile("\\d+\\.\\d+").matcher(pricePerAnnum).results()
                                .map(match -> match.group()).findFirst().orElse("")
                        + ","
                        + Pattern.compile("\\d+\\.\\d+").matcher(pricePerMonth).results()
                                .map(match -> match.group()).findFirst().orElse("")
                        + "," + capacity
                        + ","
                        + fileTypesSupported + "," +
                        specialFeatures.toString() + "," + platformCompatibility + "," + url
                        + ","
                        + contactEmail
                        + "," + contactNumber);

                pricingFileWriter.append("\n");
            }

            // Flush and close writer
            pricingFileWriter.flush();
            pricingFileWriter.close();

        } catch (Exception e) {
            // Print stack trace if error occurs
            e.printStackTrace();
        } finally {
            // Quit driver to close browser
            driver.quit();
        }
    }
}