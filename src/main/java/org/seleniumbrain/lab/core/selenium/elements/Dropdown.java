package org.seleniumbrain.lab.core.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@ScenarioScope
public class Dropdown extends BaseElement {

    public String getSelectedValue(WebElement element) {
        return new Select(element).getFirstSelectedOption().getText();
    }

    public void selectByIndex(WebElement element, int index) {
        new Select(element).selectByIndex(index);
    }

    public void selectByValue(WebElement element, String value) {
        new Select(element).selectByValue(value);
    }

    public void selectByVisibleText(WebElement element, String text) {
        new Select(element).selectByVisibleText(text);
    }

    public void selectAll(WebElement element, List<String> options) {
        new Select(element).getOptions()
                .forEach(option -> {
                    if(options.contains(option.getText()))
                        option.click();
                });
    }

    public Set<String> getOptions(WebElement element) {
        waits.untilElementToBeClickable(element).click();
        List<WebElement> options = new Select(element).getOptions();
        return options.stream().map(WebElement::getText).collect(Collectors.toSet());
    }

    public List<WebElement> getOptionItems(WebElement element) {
        waits.untilElementToBeClickable(element).click();
        return new Select(element).getOptions();
    }

    public String getText(WebElement element) {
        return new Select(element).getFirstSelectedOption().getText();
    }
}
