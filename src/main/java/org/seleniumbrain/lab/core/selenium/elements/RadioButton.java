package org.seleniumbrain.lab.core.selenium.elements;

import org.openqa.selenium.WebElement;

public class RadioButton extends BaseElement {

    /**
     * Use this to select a radio button
     *
     * @param element {@link WebElement} radio element to be selected
     */
    public void select(WebElement element) {
        waits.untilElementToBeClickable(element);
        if (!element.isSelected()) element.click();
    }

}
