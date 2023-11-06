package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.java.*;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.cucumber.spring.ApplicationContextUtil;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CucumberHooks {

    private int stepIndex = 0;

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils driverUtil;

    @SneakyThrows
    @Before
    public void createBrowserSession(Scenario scenario) {
        driverFactory.initiateWebDriverSession();
    }

    @AfterStep(order = 100)
    public void countStepIndex() {
        stepIndex += 1;
    }

    @After
    public void afterScenarioHook(Scenario scenario) throws MalformedURLException {
        byte[] screenshot = driverUtil.getScreenshotInBytes();
        scenario.attach(screenshot, "image/png", "step screenshot");
        driverFactory.getDriver().quit();
    }

    @AfterAll
    public static void afterAllMethod() {
    }
}

//        ChromeOptions browserOptions = new ChromeOptions();
//        browserOptions.setPlatformName("Windows 11");
//        browserOptions.setBrowserVersion("117");
//
//        Map<String, Object> sauceOptions = new HashMap();
//        sauceOptions.put("username", "");
//        sauceOptions.put("accessKey", "");
//        sauceOptions.put("build", "selenium-build-AH1I6");
//        sauceOptions.put("name", "saucelab-training-test");
//
//        browserOptions.setCapability("sauce:options", sauceOptions);
//
//        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
//        driver = new RemoteWebDriver(url, browserOptions);
//
