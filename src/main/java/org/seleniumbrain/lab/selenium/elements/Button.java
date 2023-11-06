package org.seleniumbrain.lab.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ScenarioScope
public class Button {

    @Autowired
    private WebDriverUtils driverUtils;

    @Autowired
    private WebDriverWaits waits;

    public void click(WebElement element) {
        element.click();
    }
}
