package org.seleniumbrain.lab.core.selenium.test;

import org.openqa.selenium.WebElement;

public interface Filter {
    public WebElement getFilterElement();
    public String getFilterOn();
}
