package org.seleniumbrain.lab.cucumber.stepdefinitions;

import io.cucumber.java.en.*;

public class LoginFeature_StepDefinitions {

    @Given("user launch app")
    public void userLaunchApp() {
        System.out.println("User Launched the application");
    }

    @When("they login with valid credentials")
    public void userLogin() {
        System.out.println("user login with valid credentials");
    }

    @Then("they are directed to home page")
    public void directedToHomePage() {
        System.out.println("Home Page is displayed");
    }
}
