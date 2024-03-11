package org.seleniumbrain.lab.core.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class SauceLabWebDriver implements DriverEngine {

    private WebDriver driver;
    /**
     * @return
     */
    @Override
    public void createSession() {
    }

    /**
     * @return WebDriver instance for Automating in SauceLab Cloud Platform
     */
    @Override
    public WebDriver getDriver() {
        return this.driver;
    }
}
