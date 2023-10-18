package org.seleniumbrain.lab.cucumber.stepdefinitions;

import io.cucumber.java.en.*;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginFeature_StepDefinitions {


    @Autowired
    private DriverFactory driverFactory;

    @Given("user launch app")
    public void userLaunchApp() {
        System.out.println("User Launched the application");
        driverFactory.getDriver().get("https://www.amazon.com");
    }

    @When("they login with valid credentials")
    public void userLogin() {
        System.out.println("user login with valid credentials");
    }

    @Then("they are directed to home page")
    public void directedToHomePage() {
        System.out.println("user is directed to home page");
//        throw new RuntimeException("Error to check screenshot");
    }
}
