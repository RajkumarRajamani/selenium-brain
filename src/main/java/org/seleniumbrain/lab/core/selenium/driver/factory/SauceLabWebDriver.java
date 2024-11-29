package org.seleniumbrain.lab.core.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebDriver;
import org.seleniumbrain.lab.core.config.SeleniumConfigReader;
import org.seleniumbrain.lab.core.config.pojo.SeleniumConfigurations;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class SauceLabWebDriver implements DriverEngine {

    private WebDriver driver;
    private static final SeleniumConfigurations.LocalLab localLabConfig = SeleniumConfigReader.getConfigs().getLab().getLocalLab();
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
