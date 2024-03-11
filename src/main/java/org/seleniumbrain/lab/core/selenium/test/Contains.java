package org.seleniumbrain.lab.core.selenium.test;

import lombok.Data;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

@Data
public class Contains implements Operator {
    private WebElement operatorElement;
    private String operationType = "contains";

    private WebElement valueElement;
    private String value;

    private Filter filter;

    public Contains(WebElement operatorElement, WebElement valueElement, String value) {
        this.operatorElement = operatorElement;
        this.valueElement = valueElement;
        this.value = value;
    }

    @Override
    public void filter() {
        // select column name to filter on
        new Select(filter.getFilterElement()).selectByVisibleText(filter.getFilterOn());

        // select operator
        new Select(operatorElement).selectByVisibleText(operationType);

        // enter value
        valueElement.sendKeys(value);
    }
}
