package org.seleniumbrain.lab.selenium.driver.factory;

import org.openqa.selenium.WebDriver;

public interface DriverEngine {

    void createSession();

    WebDriver getDriver();

}
