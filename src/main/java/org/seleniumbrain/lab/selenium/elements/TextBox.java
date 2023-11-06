package org.seleniumbrain.lab.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@ScenarioScope
public class TextBox {

    @Autowired
    private WebDriverUtils driverUtils;

    @Autowired
    private WebDriverWaits waits;

    public String getText(WebElement element) {
        return isElementInput(element) ? element.getAttribute("value") : element.getText();
    }

    public boolean setText(WebElement element, String value) {
        Objects.requireNonNull(element);
        clearText(element);
        element.sendKeys(value);
        if(!getText(element).equals(value)) {
            element.clear();
            ((JavascriptExecutor) driverUtils.getDriver()).executeScript("arguments[0].value='" + value + "'", element);
        }
        return getText(element).equals(value);
    }

    public boolean clearText(WebElement element) {
        if (isNotEmpty(element)) element.clear(); else return true;
        if (isNotEmpty(element)) this.clearWithKeys(element); else return true;
        if (isNotEmpty(element)) this.clearWithActionClass(element); else return true;
        if (isNotEmpty(element)) this.clearWithJs(element); else return true;
        return getText(element).isBlank();
    }

    public String setFakeAndGet(WebElement element, String fakeText) {
        String oldText = getText(element);
        setText(element, fakeText);
        String newText = getText(element);
        setText(element, oldText);
        return newText;
    }

    public String setAndGetText(WebElement element, String testText) {
        setText(element, testText);
        return getText(element);
    }

    public long getTextLength(WebElement element) {
        return this.getText(element).length();
    }

    private void clearWithKeys(WebElement element) {
        log.info("Unable to clear field using element.clear(). So trying SendKeys...");
        if (System.getProperty("os.name").equalsIgnoreCase("mac"))
            element.sendKeys(Keys.chord(Keys.COMMAND, "a", Keys.DELETE));
        if (System.getProperty("os.name").equalsIgnoreCase("windows"))
            element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    private void clearWithActionClass(WebElement element) {
        log.info("Unable to clear field using WebDriver's sendkeys method. So trying Action class...");
        if (System.getProperty("os.name").equalsIgnoreCase("mac")) {
            new Actions(driverUtils.getDriver())
                    .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.CONTROL)
                    .pause(200).sendKeys(Keys.BACK_SPACE)
                    .perform();
        }

        if (System.getProperty("os.name").equalsIgnoreCase("windows")) {
            new Actions(driverUtils.getDriver())
                    .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .pause(200).sendKeys(Keys.BACK_SPACE)
                    .perform();
        }
    }

    private void clearWithJs(WebElement element) {
        log.info("Unable to clear field using Action Class. So trying JavaScripExecutor...");
        ((JavascriptExecutor) driverUtils.getDriver()).executeScript("arguments[0].value = ''", element);
    }

    private boolean isElementInput(WebElement element) {
        return Objects.requireNonNull(element).getTagName().equalsIgnoreCase("input");
    }

    public boolean isNotEmpty(WebElement element) {
        return !StringUtils.isEmpty(getText(element));
    }
}
