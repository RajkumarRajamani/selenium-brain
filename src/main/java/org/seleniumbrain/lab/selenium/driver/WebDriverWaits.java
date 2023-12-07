package org.seleniumbrain.lab.selenium.driver;

import io.cucumber.spring.ScenarioScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

@Slf4j
@Component
@ScenarioScope
public class WebDriverWaits {

    @Autowired
    private DriverFactory driverFactory;

//    private List<Class<?>> ignorableExceptions = new ArrayList<>(){
//        {
//            add(NoSuchElementException.class);
//            add(TimeoutException.class);
//            add(StaleElementReferenceException.class);
//        }
//    };

    private List<Class<?>> ignorableExceptions = List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class);

    @SneakyThrows
    public void pause(long milliseconds) {
        log.info("pause for " + milliseconds + " milliseconds...");
        Thread.sleep(milliseconds);
    }

    public void until(Function<WebDriver, Boolean> condition, long timeoutInSeconds) {
        FluentWait<WebDriver> wait = new FluentWait<>(driverFactory.getDriver())
                .pollingEvery(Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class));
        wait.until(condition);
    }

    public void until(Function<WebDriver, Boolean> condition) {
        FluentWait<WebDriver> wait = new FluentWait<>(driverFactory.getDriver()).pollingEvery(Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds())).withTimeout(Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds())).ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class));
        wait.until(condition);
    }

    public void untilPageLoadComplete(long timeoutInSeconds) {
        until(driver -> {
            boolean isPageLoaded = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            if (!isPageLoaded) log.warn("Document is loading...");
            return isPageLoaded;
        }, timeoutInSeconds);
    }

    public void untilPageLoadComplete() {
        until(driver -> {
            boolean isPageLoaded = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            if (!isPageLoaded) log.warn("Document is loading...");
            return isPageLoaded;
        });
    }

    public void untilJQueryIsLoaded(long timeoutInSeconds) {
        until(driver -> {
            boolean isJQueryCompleted = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active==0");
            if(!isJQueryCompleted) log.warn("JQuery call is in progress...");
            return isJQueryCompleted;
        }, timeoutInSeconds);
    }

    public void untilJQueryIsLoaded() {
        until(driver -> {
            boolean isJQueryCompleted = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active==0");
            if(!isJQueryCompleted) log.warn("JQuery call is in progress...");
            return isJQueryCompleted;
        });
    }

    public WebElement untilElementToBeClickable(By by, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement untilElementToBeClickable(By by) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement untilElementToBeClickable(WebElement element, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement untilElementToBeClickable(WebElement element) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    //

    public boolean untilElementToBeSelected(By by, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeSelected(by));
    }

    public boolean untilElementToBeSelected(By by) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeSelected(by));
    }

    public boolean untilElementToBeSelected(WebElement element, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeSelected(element));
    }

    public boolean untilElementToBeSelected(WebElement element) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementToBeSelected(element));
    }

    //
    public boolean untilElementSelectionStateToBe(By by, boolean selected, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementSelectionStateToBe(by, selected));
    }

    public boolean untilElementSelectionStateToBe(By by, boolean selected) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementSelectionStateToBe(by, selected));
    }

    public boolean untilElementSelectionStateToBe(WebElement element, boolean selected, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementSelectionStateToBe(element, selected));
    }

    public boolean untilElementSelectionStateToBe(WebElement element, boolean selected) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.elementSelectionStateToBe(element, selected));
    }

    //
    public List<WebElement> untilNumberOfElementsToBe(By by, int count, long maxTimeout) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(maxTimeout),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.numberOfElementsToBe(by, count));
    }

    public List<WebElement> untilNumberOfElementsToBe(By by, int count) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.numberOfElementsToBe(by, count));
    }

    //
    public List<WebElement> untilNumberOfElementsToBeLessThan(By by, int count) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.numberOfElementsToBeLessThan(by, count));
    }

    public List<WebElement> untilNumberOfElementsToBeMoreThan(By by, int count) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(by, count));
    }

    //
    public boolean untilTextToBePresentInElement(WebElement element, String text) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean untilTextToBePresentInElementValue(By by, String text) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.textToBePresentInElementValue(by, text));
    }

    public boolean untilTextToBePresentInElementValue(WebElement element, String text) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.textToBePresentInElementValue(element, text));
    }

    public boolean untilTextToBePresentInElementLocated(By by, String text) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.textToBePresentInElementLocated(by, text));
    }

    public boolean untilTextToBe(By by, String text) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.textToBe(by, text));
    }

    public boolean untilTextMatches(By by, Pattern pattern) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.textMatches(by, pattern));
    }

    public Alert untilAlertIsPresent() {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.alertIsPresent());
    }

    public WebElement untilVisibilityOf(WebElement element) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement untilVisibilityOfElementLocated(By by) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public List<WebElement> untilVisibilityOfAllElementsLocatedBy(By by) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    public List<WebElement> untilVisibilityOfAllElements(WebElement... elements) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public List<WebElement> untilVisibilityOfAllElements(List<WebElement> elements) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public List<WebElement> untilVisibilityOfNestedElementsLocatedBy(By parent, By child) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parent, child));
    }

    public List<WebElement> untilVisibilityOfNestedElementsLocatedBy(WebElement parent, By child) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parent, child));
    }

    public boolean untilInvisibilityOf(WebElement element) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public boolean untilInvisibilityOfElementLocated(By by) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public boolean untilInvisibilityOfAllElements(List<WebElement> elements) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    public boolean untilInvisibilityOfAllElements(WebElement... element) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.invisibilityOfAllElements(element));
    }

    public boolean untilInvisibilityOfElementWithText(By by, String text) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.invisibilityOfElementWithText(by, text));
    }

    public boolean untilDomAttributeToBe(WebElement element, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.domAttributeToBe(element, attribute, value));
    }

    public boolean untilDomPropertyToBe(WebElement element, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.domPropertyToBe(element, attribute, value));
    }

    public boolean untilAttributeToBe(WebElement element, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public boolean untilAttributeToBe(By by, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.attributeToBe(by, attribute, value));
    }

    public boolean untilAttributeContains(WebElement element, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public boolean untilAttributeContains(By by, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.attributeContains(by, attribute, value));
    }

    public boolean untilAttributeToBeNotEmpty(WebElement element, String attribute, String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
    }

    public boolean untilTitleContains(String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.titleContains((value)));
    }

    public boolean untilTitleIs(String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.titleIs(value));
    }

    public boolean untilUrlContains(String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.urlContains(value));
    }

    public boolean untilUrlMatches(String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.urlMatches(value));
    }

    public boolean untilUrlToBe(String value) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.urlToBe(value));
    }

    public WebDriver untilFrameToBeAvailableAndSwitchToIt(String frameName) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
    }

    public WebDriver untilFrameToBeAvailableAndSwitchToIt(WebElement frame) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
    }

    public WebDriver untilFrameToBeAvailableAndSwitchToIt(By by) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
    }

    public WebDriver untilFrameToBeAvailableAndSwitchToIt(int frameIndex) {
        return new WebDriverWait(
                driverFactory.getDriver(),
                Duration.ofSeconds(SeleniumConfigReader.getFluentMaxTimeoutInSeconds()),
                Duration.ofMillis(SeleniumConfigReader.getFluentPollingTimeoutInSeconds()))
                .ignoreAll(List.of(NoSuchElementException.class, TimeoutException.class, StaleElementReferenceException.class))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
    }
}
