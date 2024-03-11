package org.seleniumbrain.lab.core.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.seleniumbrain.lab.core.config.SystemConfig;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@ScenarioScope
public class TextBox extends BaseElement {

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
        boolean isCleard = false;
        try {
            if (isNotEmpty(element)) element.clear();
            if (isNotEmpty(element)) this.clearWithUnicode(element);
            if (isNotEmpty(element)) this.clearWithKeys(element);
            if (isNotEmpty(element)) this.clearWithActionClass(element);
            if (isNotEmpty(element)) this.clearWithJs(element);
            isCleard = getText(element).isBlank();
        } finally {
            if(isCleard) log.info("Element value is cleared and is blank.");
            else log.info("Unable to clear the element value.");
        }
        return isCleard;
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

    private void clearWithUnicode(WebElement element) {
        log.info("Unable to clear field using element.clear(). So trying delete[mac \\U+007F]/backspace[windows \\U+0008] unicode character...");
        String osName = SystemConfig.getOsName();
        while(isNotEmpty(element)) {
            int beforeLength = getText(element).length();
            switch (osName) {
                case "mac" -> element.sendKeys("\u007F");
                case "windows" -> element.sendKeys("\u0008");
            }
            int afterLength = getText(element).length();
            if(afterLength >= beforeLength || afterLength == 0) break;
        }
    }

    private void clearWithKeys(WebElement element) {
        log.info("Unable to clear field using element.clear(). So trying SendKeys...");
        switch (SystemConfig.getOsName()) {
            case "mac" -> element.sendKeys(Keys.chord(Keys.COMMAND, "a", Keys.DELETE));
            case "windows" -> element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));
        }
    }

    private void clearWithActionClass(WebElement element) {
        log.info("Unable to clear field using WebDriver's sendKeys method. So trying Action class...");
        switch (SystemConfig.getOsName()) {
            case "mac" -> new Actions(driverUtils.getDriver())
                    .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.CONTROL)
                    .pause(200).sendKeys(Keys.DELETE)
                    .perform();
            case "windows" -> new Actions(driverUtils.getDriver())
                    .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .pause(200).sendKeys(Keys.BACK_SPACE)
                    .perform();
        }
    }

    private void clearWithJs(WebElement element) {
        log.info("Unable to clear field using Action Class. So trying JavaScripExecutor...");
        ((JavascriptExecutor) driverUtils.getDriver()).executeScript("arguments[0].value = ''", element);
    }

    public boolean isNotEmpty(WebElement element) {
        return !StringUtils.isEmpty(getText(element));
    }
}
