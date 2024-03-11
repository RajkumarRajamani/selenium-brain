package org.seleniumbrain.lab.core.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class GridWebDriver implements DriverEngine {

    private WebDriver driver;

    /**
     * Initiates a new session of a given Grid to perform the test case execution
     *
     */
    @Override
    public void createSession() {
    }

    /**
     * @return WebDriver instance for Grid Execution
     */
    @Override
    public WebDriver getDriver() {
        return this.driver;
    }
}
