package org.seleniumbrain.lab.core.selenium.driver;

import lombok.Getter;

@Getter
public enum Labs {

    LOCAL("local"),
    GRID("grid"),
    SAUCE_LAB("sauce-lab"),
    PERFECTO("perfecto"),
    DOCKER("docker");

    private final String name;
    Labs(String name) {
        this.name = name;
    }

}
