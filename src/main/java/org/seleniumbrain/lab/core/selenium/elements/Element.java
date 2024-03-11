package org.seleniumbrain.lab.core.selenium.elements;

import org.openqa.selenium.WebElement;

import java.util.function.Function;

public interface Element extends Function<WebElement, Object> {
}
