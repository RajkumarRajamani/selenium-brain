package org.seleniumbrain.lab.core.selenium.elements;

import org.openqa.selenium.WebElement;

public class Checkbox extends BaseElement {

    /**
     * Use this to select a checkbox
     *
     * @param element {@link WebElement} checkbox element to be checked
     */
    public void select(WebElement element) {
        waits.untilElementToBeClickable(element);
        if (!element.isSelected()) element.click();
    }

    /**
     * Use this to deselect a checkbox
     *
     * @param element {@link WebElement} checkbox element to be unchecked
     */
    public void deSelect(WebElement element) {
        waits.untilElementToBeClickable(element);
        if (element.isSelected()) element.click();
    }

}
