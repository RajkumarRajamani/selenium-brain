package org.seleniumbrain.lab.selenium.driver;

public enum Browsers {

    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");


    private String name;
    Browsers(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
