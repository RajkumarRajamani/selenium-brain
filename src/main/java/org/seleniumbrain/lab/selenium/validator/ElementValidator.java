package org.seleniumbrain.lab.selenium.validator;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.TriFunction;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public interface ElementValidator extends TriFunction<WebDriver, WebElement, String, Boolean> {

    static boolean isElementInput(WebElement element) {
        return Objects.requireNonNull(element).getTagName().equalsIgnoreCase("input");
    }

    static String getText(WebElement element) {
        return isElementInput(element) ? element.getAttribute("value") : element.getText();
    }

    static boolean clearText(WebElement element) {
        while(!StringUtils.isEmpty(getText(element)))
            element.sendKeys("\u0008");
        return getText(element).isBlank();
    }

    static boolean setText(WebElement element, String value) {
        Objects.requireNonNull(element);
        if(value.isBlank())
            clearText(element);
        else
            element.sendKeys(value);
        return getText(element).equals(value);
    }


    static boolean clearAndSetText(WebElement element, String value) {
        return clearText(element) && setText(element, value);
    }

    static String checkField(WebElement element, String testText) {
        String oldText = getText(element);
        clearAndSetText(element, testText);
        String newText = getText(element);
        clearAndSetText(element, oldText);
        return newText;
    }

}
