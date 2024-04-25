package org.seleniumbrain.lab.cucumber.stepdefinitions;

import com.azure.security.keyvault.secrets.SecretClient;
import io.cucumber.java.en.*;
import org.seleniumbrain.lab.core.cucumber.spring.configure.ScenarioState;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.SharedStateKey;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.core.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.snippet.pagerepositories.demo.SwagLabLoginPageOR;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginFeature_StepDefinitions {


    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils driverUtils;

    @Autowired
    private SecretClient azureKeyVaulet;

    @Autowired
    private SwagLabLoginPageOR homePageOR;

    @Autowired
    private ScenarioState scenarioState;

    String a = "sd";
    @Given("user launch app")
    public void userLaunchApp() {
        System.out.println("User Launched the application");
        driverFactory.getDriver().get("https://www.saucedemo.com/");
//        System.out.println(azureKeyVaulet.getSecret("h2db"));
    }

    @When("they login with valid credentials")
    public void userLogin() {
        System.out.println("user login with valid credentials");
        Assertions assertions = new Assertions();
//        assertions.addKnownFailureLabels("UserName Field", "ID1000");
        homePageOR
                .withAssertion(assertions, homePageOR);
//                .enterUserName("standard_user");
//                .enterPassword("secret_sauce")
//                .login()
//                .openSideMenu()
//                .navigateToAboutPage();
        assertions.assertAll();
    }

    @Then("they are directed to home page")
    public void directedToHomePage() {
        System.out.println("user is directed to home page");
        System.out.println("Scenario State Shared Value: " + scenarioState.getCacheText().get(SharedStateKey.NAME));
    }
}
