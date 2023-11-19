package org.seleniumbrain.lab.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@ScenarioScope
public class BaseElement {

    @Autowired
    public WebDriverUtils driverUtils;

    @Autowired
    public WebDriverWaits waits;

    public void click(WebElement element) {
        waits.untilElementToBeClickable(element);
        element.click();
    }

    private boolean isElementInput(WebElement element) {
        return Objects.requireNonNull(element).getTagName().equalsIgnoreCase("input");
    }

    public String getText(WebElement element) {
        return isElementInput(element) ? element.getAttribute("value") : element.getText();
    }

    public long getTextLength(WebElement element) {
        return this.getText(element).length();
    }

}
