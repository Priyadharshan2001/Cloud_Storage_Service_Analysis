// Define the package for the Azure web scraper class
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
// Import Java collections classes
import java.util.ArrayList;
import java.util.List;
// Import Java regex classes for pattern matching
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Main class for Azure cloud services scraper
public class Azure {

    // Constant for Microsoft provider name
    private static final String MICROSOFT = "Microsoft";

    // Main method to start the scraper
    public static void main(String[] args) {
        // Create instance of Azure scraper
        Azure scraper = new Azure();
        // Initialize scraper with output directory
        scraper.init("./temp");
    }

    // Method to initialize the web scraper
    public void init(String directory) {
        // Initialize Chrome WebDriver
        WebDriver driver = new ChromeDriver();
        // Create list to store cloud service data
        List<CloudService> cloudServices = new ArrayList<>();

        // Navigate to Azure pricing page
        driver.get("https://www.microsoft.com/en-ca/microsoft-365/Azure/compare-Azure-plans");

        // Find main container element for pricing cards
        WebElement baharKaContainer = driver.findElement(
                By.cssSelector("div.sku-cards.grid.g-col-12.g-start-1.aem-GridColumn.aem-GridColumn--default--12"));

        // Create and populate first cloud service object
        CloudService cloudService = new CloudService();
        // Set provider name
        cloudService.setProvider(MICROSOFT);
        // Set plan name from webpage
        cloudService.setPlanName(baharKaContainer.findElement(By.cssSelector(
                "div.sku-title.oc-product-title.px-4.text-center.g-col-12.g-start-1.g-col-sm-6.g-col-md-5.g-col-lg-3.g-start-sm-1.g-start-md-2.g-start-lg-1"))
                .findElement(By.tagName("span")).getText());
        // Set annual price from webpage
        cloudService.setPricePerAnnum(baharKaContainer.findElement(By.cssSelector(
                "div.sku-card-price.px-4.text-center.g-col-12.g-start-1.g-col-sm-6.g-col-md-5.g-col-lg-3.g-start-sm-1.g-start-md-2.g-start-lg-1"))
                .findElement(By.cssSelector("span.oc-list-price.font-weight-semibold.text-primary"))
                .getText());
        // Set monthly price from webpage
        cloudService.setPricePerMonth(
                baharKaContainer.findElement(By.cssSelector("div.w-col-7.w-md-col-10.mx-auto"))
                        .findElement(By.cssSelector("span.oc-token.oc-list-price")).getText());
        // Set capacity from webpage
        cloudService.setCapacity(baharKaContainer.findElement(By.cssSelector(".card-body"))
                .findElement(By.xpath("//*[@id=\"custom-list-item-oce04e\"]/div/p/span")).getText());
        // Set special features from webpage
        cloudService.setSpecialFeatures(
                "\"- " + (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oca298\"]/div/p/span"))
                        .getText()) + "\"");
        // Set platform compatibility from webpage
        cloudService.setPlatformCompatibility(
                (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oc4ada\"]/div/p/span"))
                        .getText()));
        // Set current URL
        cloudService.setUrl(driver.getCurrentUrl());
        // Add to services list
        cloudServices.add(cloudService);

        // Create and populate second cloud service object
        CloudService cloudService1 = new CloudService();
        // Set provider name
        cloudService1.setProvider(MICROSOFT);
        // Set plan name from webpage
        cloudService1.setPlanName(baharKaContainer.findElement(By.cssSelector(
                "div.sku-title.oc-product-title.px-4.text-center.g-col-12.g-start-1.g-col-sm-6.g-col-md-5.g-col-lg-3.g-start-sm-7.g-start-md-7.g-start-lg-4"))
                .findElement(By.tagName("span")).getText());
        // Set hardcoded annual price
        cloudService1.setPricePerAnnum("CAD $79.00");
        // Set monthly price from webpage
        cloudService1.setPricePerMonth((baharKaContainer.findElement(By.xpath(
                "/html/body/div[3]/div/div[2]/main/div/div/div/div[3]/div/div/div/div/section/div/div[2]/div/div/div[1]/div/div/div/div/div/div[1]/div[2]/div[1]/div[2]/div[6]/div/div/div[2]/a/div[2]/div/div/div/span/span"))
                .getText()));
        // Set capacity from webpage
        cloudService1.setCapacity(baharKaContainer.findElement(By.cssSelector(".card-body"))
                .findElement(By.xpath("//*[@id=\"custom-list-item-ocda3c\"]/div/p/span")).getText());
        // Set special features from webpage
        cloudService1.setSpecialFeatures(
                "\"- " + (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oca298\"]/div/p/span"))
                        .getText()) + "\"");
        // Set platform compatibility from webpage
        cloudService1.setPlatformCompatibility(
                (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oc4ada\"]/div/p/span"))
                        .getText()));
        // Set current URL
        cloudService1.setUrl(driver.getCurrentUrl());
        // Add to services list
        cloudServices.add(cloudService1);

        // Create and populate third cloud service object
        CloudService cloudService2 = new CloudService();
        // Set provider name
        cloudService2.setProvider(MICROSOFT);
        // Set plan name from webpage
        cloudService2.setPlanName(baharKaContainer.findElement(By.cssSelector(
                "div.sku-title.oc-product-title.px-4.text-center.g-col-12.g-start-1.g-col-sm-6.g-col-md-5.g-col-lg-3.g-start-sm-1.g-start-md-2.g-start-lg-7"))
                .findElement(By.tagName("span")).getText());
        // Set hardcoded annual price (note: incorrectly setting to cloudService1)
        cloudService1.setPricePerAnnum("CAD $8.00");
        // Set monthly price from webpage
        cloudService2.setPricePerMonth((baharKaContainer.findElement(By.xpath(
                "/html/body/div[3]/div/div[2]/main/div/div/div/div[3]/div/div/div/div/section/div/div[2]/div/div/div[1]/div/div/div/div/div/div[1]/div[2]/div[2]/div[1]/div[6]/div/div/ul/li[1]/a/div[2]/div/div/div/span[1]/span"))
                .getText()));
        // Set capacity from webpage
        cloudService2.setCapacity(baharKaContainer.findElement(By.cssSelector(".card-body"))
                .findElement(By.xpath("//*[@id=\"custom-list-item-oc147e\"]/div/p/span")).getText());
        // Set special features from webpage
        cloudService2.setSpecialFeatures(
                "\"- " + (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oc26f8\"]/div/p/span"))
                        .getText()) + "\"");
        // Set platform compatibility from webpage
        cloudService2.setPlatformCompatibility(
                (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oc4ada\"]/div/p/span"))
                        .getText()));
        // Set current URL
        cloudService2.setUrl(driver.getCurrentUrl());
        // Add to services list
        cloudServices.add(cloudService2);

        // Create and populate fourth cloud service object
        CloudService cloudService3 = new CloudService();
        // Set provider name
        cloudService3.setProvider(MICROSOFT);
        // Set plan name from webpage
        cloudService3.setPlanName(baharKaContainer.findElement(By.cssSelector(
                "div.sku-title.oc-product-title.px-4.text-center.g-col-12.g-start-1.g-col-sm-6.g-col-md-5.g-col-lg-3.g-start-sm-7.g-start-md-7.g-start-lg-10"))
                .findElement(By.tagName("span")).getText());
        // Set annual price from webpage (note: incorrectly setting to cloudService1)
        cloudService1.setPricePerAnnum((baharKaContainer
                .findElement(By.xpath("//*[@id=\"sku-card-oc0cb3\"]/div[5]/div[1]/div/p[2]/span[2]"))
                .getText()));
        // Set monthly price from webpage
        cloudService3.setPricePerMonth((baharKaContainer
                .findElement(By.xpath("//*[@id=\"sku-card-oc0cb3\"]/div[5]/div[1]/div/p[2]/span[2]"))
                .getText()));
        // Set capacity from webpage
        cloudService3.setCapacity(baharKaContainer.findElement(By.cssSelector(".card-body"))
                .findElement(By.xpath("//*[@id=\"custom-list-item-oc5bb0\"]/div/p/span")).getText());
        // Set special features from webpage
        cloudService3.setSpecialFeatures(
                "\"- " + (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oc2279\"]/div/p/span"))
                        .getText()) + "\"");
        // Set platform compatibility from webpage
        cloudService3.setPlatformCompatibility(
                (baharKaContainer
                        .findElement(By.xpath(
                                "//*[@id=\"custom-list-item-oc0e28\"]/div/p/span"))
                        .getText()));
        // Set current URL
        cloudService3.setUrl(driver.getCurrentUrl());
        // Add to services list
        cloudServices.add(cloudService3);

        // Write collected data to CSV file
        writeToCsv(cloudServices, directory);

        // Quit driver to close browser
        driver.quit();
    }

    // Method to write data to CSV file
    private static void writeToCsv(List<CloudService> cloudServices, String directory) {
        // Define output file path
        String path = directory + "/Azure.csv";
        // Create File object for output
        File file = new File(path);
        // Create parent directories if they don't exist
        file.getParentFile().mkdirs();

        // Try-with-resources for FileWriter to ensure proper resource cleanup
        try (FileWriter csvWriter = new FileWriter(path)) {
            // Write CSV header row
            csvWriter.append(
                    "Provider,Plan Name,Price per annum,Price per month,Capacity,File types supported,Special features,Platform compatibility,URL,Contact Email,Contact Number\n");

            // Write each service's data to CSV
            for (CloudService service : cloudServices) {
                csvWriter.append(service.getProvider()).append(",");
                csvWriter.append(service.getPlanName()).append(",");
                csvWriter.append(service.getPricePerAnnum()).append(",");
                csvWriter.append(service.getPricePerMonth()).append(",");
                csvWriter.append(service.getCapacity()).append(",");
                csvWriter.append(","); // Empty for file types supported
                csvWriter.append(service.getSpecialFeatures()).append(",");
                csvWriter.append(service.getPlatformCompatibility()).append(",");
                csvWriter.append(service.getUrl()).append(",");
                csvWriter.append(",\n"); // Empty for contact info
            }

            // Log success message
            System.out.println("Data successfully written to CloudServices.csv");
        } catch (IOException e) {
            // Log error message
            System.err.println("Error while writing to CSV file: " + e.getMessage());
        }
    }
}

// Class representing a cloud service offering
class CloudService {
    // Private fields for service attributes
    private String provider;
    private String planName;
    private String pricePerAnnum;
    private String pricePerMonth;
    private String capacity;
    //private String fileTypesSupported;
    private String specialFeatures;
    private String platformCompatibility;
    private String url;

    // Getter for provider
    public String getProvider() {
        return provider;
    }

    // Setter for provider
    public void setProvider(String provider) {
        this.provider = provider;
    }

    // Getter for plan name
    public String getPlanName() {
        return planName;
    }

    // Setter for plan name
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    // Getter for annual price
    public String getPricePerAnnum() {
        return pricePerAnnum;
    }

    // Setter for annual price
    public void setPricePerAnnum(String pricePerAnnum) {
        this.pricePerAnnum = pricePerAnnum;
    }

    // Getter for monthly price
    public String getPricePerMonth() {
        return pricePerMonth;
    }

    // Setter for monthly price
    public void setPricePerMonth(String pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    // Getter for capacity
    public String getCapacity() {
        return capacity;
    }

    // Setter for capacity with regex parsing
    public void setCapacity(String capacity) {
        // Pattern to match storage sizes (e.g., "1 GB", "1 TB")
        Pattern pattern = Pattern.compile("(\\d+\\s?(GB|TB))");
        Matcher matcher = pattern.matcher(capacity);

        // If pattern matches, extract storage size
        if (matcher.find()) {
            this.capacity = matcher.group(1);
        } else {
            // Fallback to empty string if no match
            this.capacity = "";
        }
    }

    // Getter for special features
    public String getSpecialFeatures() {
        return specialFeatures;
    }

    // Setter for special features
    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    // Getter for platform compatibility with formatting
    public String getPlatformCompatibility() {
        return "\"- " + platformCompatibility.replace("Works on ", "").replace(",", "\n- ").replace("and ",
                "") + "\"";
    }

    // Setter for URL
    public void setUrl(String url) {
        this.url = url;
    }

    // Getter for URL
    public String getUrl() {
        return url;
    }

    // Setter for platform compatibility
    public void setPlatformCompatibility(String platformCompatibility) {
        this.platformCompatibility = platformCompatibility;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "CloudService{" +
                "provider='" + provider + '\'' +
                ", planName='" + planName + '\'' +
                ", pricePerAnnum='" + pricePerAnnum + '\'' +
                ", pricePerMonth='" + pricePerMonth + '\'' +
                ", capacity='" + capacity + '\'' +
                ", specialFeatures='" + specialFeatures + '\'' +
                ", platformCompatibility='" + platformCompatibility + '\'' +
                '}';
    }
}