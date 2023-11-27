package org.seleniumbrain.lab.selenium.test;

import org.openqa.selenium.WebElement;

public interface Filter {
    public WebElement getFilterElement();
    public String getFilterOn();
}
