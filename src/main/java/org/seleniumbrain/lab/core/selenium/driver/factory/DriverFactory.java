package org.seleniumbrain.lab.core.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.seleniumbrain.lab.core.config.SeleniumConfigReader;
import org.seleniumbrain.lab.core.selenium.driver.Labs;
import org.seleniumbrain.lab.utility.RetryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Slf4j
@Component
@ScenarioScope
public class DriverFactory {

    @Autowired
    private LocalWebDriver localWebDriver;

    @Autowired
    private GridWebDriver gridWebDriver;

    @Autowired
    private SauceLabWebDriver sauceLabWebDriver;

    @Autowired
    private PerfectoWebDriver perfectoWebDriver;

    public void initiateWebDriverSession() {
        String labName = SeleniumConfigReader.getTestLabName();
        log.info(MessageFormat.format("Initiating WebDriver Session for {0} - TestLab Environment", labName));

        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .run(() -> {
                    try {
                        switch (Labs.valueOf(labName.toUpperCase())) {
                            case LOCAL -> localWebDriver.createSession();

                            case GRID -> gridWebDriver.createSession();

                            case SAUCE_LAB -> sauceLabWebDriver.createSession();

                            case PERFECTO -> perfectoWebDriver.createSession();

                            case DOCKER -> {}
                        }
                    } catch (Exception e) {
                        this.quiteWebDriver();
                    }
                    return true;
                });
    }

    public WebDriver getDriver() {
        String labName = SeleniumConfigReader.getTestLabName();

        switch (Labs.valueOf(labName.toUpperCase())) {
            case LOCAL -> {
                return localWebDriver.getDriver();
            }

            case GRID -> {
                return gridWebDriver.getDriver();
            }

            case SAUCE_LAB -> {
                return sauceLabWebDriver.getDriver();
            }

            case PERFECTO -> {
                return perfectoWebDriver.getDriver();
            }

            case DOCKER -> {}
        }

        return null;
    }

    public void quiteWebDriver() {
        log.info("Attempting to quite all browser sessions");
        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .forceRetryIgnoringExceptions(() -> {
                    if(this.getDriver() != null)
                        this.getDriver().quit();
                    return true;
                });
    }
}
