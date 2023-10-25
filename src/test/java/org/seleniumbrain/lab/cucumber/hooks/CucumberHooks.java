package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.cucumber.spring.ApplicationContextUtil;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;

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
        driverFactory.getDriver().get("https://www.google.com");
        Thread.sleep(3000);
    }

    @AfterStep
    public void countStepIndex() {
        stepIndex = stepIndex + 1;
    }

    @AfterStep
    public void findDriver(Scenario scenario) {
        WebDriver driver = ApplicationContextUtil.getBean(DriverFactory.class).getDriver();
        System.out.println(scenario.getName() + " : " + driver.toString());
    }

    @After
    public void afterScenarioHook(Scenario scenario) throws MalformedURLException {
        byte[] screenshot = driverUtil.getScreenshotInBytes();
        scenario.attach(screenshot, "image/png", "step screenshot");
        driverFactory.getDriver().quit();
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
