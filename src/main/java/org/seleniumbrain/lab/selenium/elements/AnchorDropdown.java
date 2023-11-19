package org.seleniumbrain.lab.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Sometimes, dropdown will be implemented without default {@link org.openqa.selenium.support.ui.Select} tag.</p>
 *
 * <p>For Example, on clicking a element, we might see a floating element showing a list of values to pick from.
 * This can not be dealt with {@link org.openqa.selenium.support.ui.Select} selenium option.</p>
 *
 * <p>{@code AnchorDropdown} element serves the purpose.
 * We can start using it by initializing the method chaining approach with method "with" and supplying
 * root anchor element [that opens op floating popup], and 'By' selectors to list of pop-up elements.</p>
 */
@Slf4j
@Component
@ScenarioScope
public class AnchorDropdown extends BaseElement {

    public AnchorDropdownAction with(WebElement rootAnchorElement, By listOptionsLocator) {
        return new AnchorDropdownAction(rootAnchorElement, listOptionsLocator);
    }

    public class AnchorDropdownAction {
        private final WebElement divDropdownElement;
        private final By locationForOptionList;

        public AnchorDropdownAction(WebElement divDropdownElement, By locationForOptionList) {
            this.divDropdownElement = divDropdownElement;
            this.locationForOptionList = locationForOptionList;
        }

        public String getSelectedText() {
            return getText(divDropdownElement);
        }

        public void selectByIndex(int index) {
            divDropdownElement.click();
            List<WebElement> options = driverUtils.getDriver().findElements(locationForOptionList);
            options.get(index).click();
        }

        public void selectByVisibleText(String visibleText) {
            divDropdownElement.click();
            List<WebElement> options = driverUtils.getDriver().findElements(locationForOptionList);
            for (WebElement option : options) {
                if (option.getText().equals(visibleText)) option.click();
            }
        }

        public void selectAll(List<String> options) {
            this.getOptionItems()
                    .forEach(option -> {
                        if(options.contains(option.getText()))
                            option.click();
                    });
        }

        public List<String> getOptions() {
            divDropdownElement.click();
            List<WebElement> options = driverUtils.getDriver().findElements(locationForOptionList);
            return options.stream().map(WebElement::getText).collect(Collectors.toList());
        }

        public List<WebElement> getOptionItems() {
            divDropdownElement.click();
            return driverUtils.getDriver().findElements(locationForOptionList);
        }
    }


}