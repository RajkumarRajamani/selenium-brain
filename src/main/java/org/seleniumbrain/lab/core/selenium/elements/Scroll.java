package org.seleniumbrain.lab.core.selenium.elements;


import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ScenarioScope
public class Scroll extends BaseElement {

    public void scrollPageTillBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driverUtils.getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    public void scrollElementIntoTopView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driverUtils.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollElementIntoBottomView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driverUtils.getDriver();
        js.executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public void scrollElementVerticalBy(WebElement element, int yPixels) {
        System.out.println("vertical scroll start");
        JavascriptExecutor js = (JavascriptExecutor) driverUtils.getDriver();
        js.executeScript("arguments[0].scrollBy(0, " + yPixels + ")", element);
        System.out.println("vertical scroll end");
    }

    public void scrollElementHorizontalBy(WebElement element, int xPixels) {
        JavascriptExecutor js = (JavascriptExecutor) driverUtils.getDriver();
        js.executeScript("arguments[0].scrollBy(" + xPixels + ", 0)", element);
    }

    public void scrollElementBy(WebElement element, int xPixels, int yPixels) {
        JavascriptExecutor js = (JavascriptExecutor) driverUtils.getDriver();
        js.executeScript("arguments[0].scrollBy(" + xPixels + ", " + yPixels + ")", element);
    }
}
