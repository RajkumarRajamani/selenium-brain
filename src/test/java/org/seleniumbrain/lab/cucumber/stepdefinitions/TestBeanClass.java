package org.seleniumbrain.lab.cucumber.stepdefinitions;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class TestBeanClass {

    public void testMethod() {
        System.out.println("Test method called");
    }

}
