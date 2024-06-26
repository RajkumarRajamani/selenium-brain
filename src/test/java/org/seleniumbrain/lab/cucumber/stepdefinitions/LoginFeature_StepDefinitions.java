package org.seleniumbrain.lab.cucumber.stepdefinitions;

import com.azure.security.keyvault.secrets.SecretClient;
import io.cucumber.java.en.*;
import org.seleniumbrain.lab.core.cucumber.spring.configure.ScenarioState;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.SharedStateKey;
import org.seleniumbrain.lab.cucumber.hooks.LobSynchronizer;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.core.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.snippet.pagerepositories.demo.SwagLabLoginPageOR;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

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
//        driverFactory.getDriver().get("https://www.saucedemo.com/");
    }

    @When("they login with valid credentials")
    public void userLogin() {
        System.out.println("user login with valid credentials");
        Assertions assertions = new Assertions();
//        assertions.addKnownFailureLabels("UserName Field", "ID1000");
//        try {
//            Thread.sleep(new Random().nextInt(10000, 15000));
//        } catch (Exception ignored) {}
//        homePageOR
//                .withAssertion(assertions, homePageOR)
//                .enterUserName("standard_user")
//                .enterPassword("secret_sauce")
//                .login()
//                .openSideMenu();
//                .navigateToAboutPage();
//        try {
//            driverFactory.getDriver().get("https://www.saucedemo.com/" + scenarioState.getText(SharedStateKey.dummyText));
//        } catch (Exception ignored) {}
        driverUtils.attachStepLogInfo("C:\\abc\\asdjk\\asdj\\sdskjd\\jdj.txt");
        assertions.assertAll();
    }

    @Then("they are directed to home page")
    public void directedToHomePage() {
        System.out.println("user is directed to home page");
        System.out.println("Scenario State Shared Value: " + scenarioState.getCacheText().get(SharedStateKey.NAME));
    }
}
