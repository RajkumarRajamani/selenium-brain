package org.seleniumbrain.lab.testng;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SauceLab {

    public static void main(String[] args) throws MalformedURLException {

//        ChromeOptions options = new ChromeOptions();
//        options.setPlatformName("Windows 11");
//        options.setBrowserVersion("latest");
//
//        Map<String, Object> sauceOptions = new HashMap<>();
//        sauceOptions.put("username", System.getenv("oauth-rajoviyaa.s-576ad"));
//        sauceOptions.put("accessKey", System.getenv("51b9242c-171c-4499-8e97-60e3123990a3"));
//        sauceOptions.put("name", "SauceLab Test Attempt 1");
//
//        options.setCapability("sauce:options", sauceOptions);
//        URL url = new URL("https://oauth-rajoviyaa.s-576ad:51b9242c-171c-4499-8e97-60e3123990a3@ondemand.eu-central-1.saucelabs.com:443/wd/hub");
//
//        WebDriver driver = new RemoteWebDriver(url, options);
//
//        driver.get("https://www.google.com");
//
//
//        System.out.println(driver.getCurrentUrl());
//
//        driver.quit();

        try {
            // Start ChromeDriver as a service (managed automatically)
            ChromeDriverService service = ChromeDriverService.createDefaultService();
            service.start();

            // Create RemoteWebDriver instance pointing to the local driver service
            WebDriver driver = new RemoteWebDriver(service.getUrl(), new ChromeOptions());

            // Open a webpage
            driver.get("https://www.example.com");
            System.out.println("Page title is: " + driver.getTitle());

            // Quit the browser
            driver.quit();

            // Stop the driver service
            service.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
