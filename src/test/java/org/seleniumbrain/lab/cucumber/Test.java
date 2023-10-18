package org.seleniumbrain.lab.cucumber;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws MalformedURLException {
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 11");
        browserOptions.setBrowserVersion("117");

        Map<String, Object> sauceOptions = new HashMap();
        sauceOptions.put("username", "oauth-rajoviyaa.s-b07bf");
        sauceOptions.put("accessKey", "3d34e79c-04b9-4b03-88f4-d813048065a1");
        sauceOptions.put("build", "selenium-build-AH1I6");
        sauceOptions.put("name", "saucelab-training-test");

        browserOptions.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        RemoteWebDriver driver = new RemoteWebDriver(url, browserOptions);

        driver.get("https://www.google.com");
    }
}
