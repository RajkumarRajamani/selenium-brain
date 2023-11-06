package org.seleniumbrain.lab.selenium.elements;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ScenarioScope
public class Dropdown {

    @Autowired
    private WebDriverUtils driverUtils;

    @Autowired
    private WebDriverWaits waits;

    public String getVisibleText(WebElement element) {
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

    public boolean isMultiSelect(WebElement element) {
        return new Select(element).isMultiple();
    }

    public DivDropdown withDivDropdownStyle(WebElement divDropdownElement, By optionsLocator) {
        return new DivDropdown(divDropdownElement, optionsLocator);
    }

    /**
     * <p>In some cases, dropdown control will be created without default 'Select' tag, such as by using div or other tags.
     * This class assumes that the dropdown is made up of div tag.</p>
     *
     */
    public class DivDropdown {

        private final WebElement divDropdownElement;
        private final By locationForOptionList;

        public DivDropdown(WebElement divDropdownElement, By locationForOptionList) {
            this.divDropdownElement = divDropdownElement;
            this.locationForOptionList = locationForOptionList;
        }

        public String getSelectedText() {
            return divDropdownElement.getText();
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
                if (option.getText().equals(visibleText))
                    option.click();
            }
        }


    }


}
