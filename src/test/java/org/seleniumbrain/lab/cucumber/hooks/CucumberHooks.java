package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.SneakyThrows;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;

public class CucumberHooks {

    private int stepIndex = 0;

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    @Autowired
    private DriverFactory driverFactory;

    @SneakyThrows
    @Before
    public void createBrowserSession(Scenario scenario) {
        driverFactory.initiateWebDriverSession();
        driverFactory.getDriver().get("https://www.google.com");
        Thread.sleep(3000);
    }

    @AfterStep
    public void countStepIndex() {
        stepIndex = stepIndex + 1;
    }
    @After
    public void afterScenarioHook(Scenario scenario) throws MalformedURLException {
        driverFactory.getDriver().quit();
    }
}

//        ChromeOptions browserOptions = new ChromeOptions();
//        browserOptions.setPlatformName("Windows 11");
//        browserOptions.setBrowserVersion("117");
//
//        Map<String, Object> sauceOptions = new HashMap();
//        sauceOptions.put("username", "oauth-rajoviyaa.s-b07bf");
//        sauceOptions.put("accessKey", "3d34e79c-04b9-4b03-88f4-d813048065a1");
//        sauceOptions.put("build", "selenium-build-AH1I6");
//        sauceOptions.put("name", "saucelab-training-test");
//
//        browserOptions.setCapability("sauce:options", sauceOptions);
//
//        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
//        driver = new RemoteWebDriver(url, browserOptions);
//
