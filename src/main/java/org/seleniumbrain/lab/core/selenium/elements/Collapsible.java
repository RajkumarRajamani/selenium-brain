package org.seleniumbrain.lab.core.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ScenarioScope
public class Collapsible extends BaseElement {

    public boolean isAriaCollapsed(WebElement element) {
        return element.getAttribute("aria-expanded").equals("false");
    }

    public boolean isAriaExpanded(WebElement element) {
        return element.getAttribute("aria-expanded").equals("true");
    }

    public void collapseAriaIfExpanded(WebElement element) {
        if(this.isAriaExpanded(element))
            element.click();
    }

    public void expandAriaIfCollapsed(WebElement element) {
        if(this.isAriaCollapsed(element))
            element.click();
    }

}
