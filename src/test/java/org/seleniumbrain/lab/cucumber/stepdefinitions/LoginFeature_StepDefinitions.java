package org.seleniumbrain.lab.cucumber.stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.cucumber.spring.ApplicationContextUtil;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginFeature_StepDefinitions {


    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils driverUtils;

    @Given("user launch app")
    public void userLaunchApp() {
        System.out.println("User Launched the application");
        driverFactory.getDriver().get("https://www.selenium.dev/");
        WebDriverWaits wait = ApplicationContextUtil.getBean(WebDriverWaits.class);
        System.out.println("Waiting...");
        wait.pause(10000);
        WebElement element = ApplicationContextUtil.getBean(DriverFactory.class).getDriver().findElement(By.xpath("//span[text()='Downloads']"));
        element.click();
        System.out.println("Waiting over...");
        driverUtils.attachScreenshot("Test");
        driverUtils.attachStepLogInfo("Log info test");
    }

    @When("they login with valid credentials")
    public void userLogin() {
        System.out.println("user login with valid credentials");
        System.out.println(ApplicationContextUtil.getBean(DriverFactory.class).getDriver().getTitle());
    }

    @Then("they are directed to home page")
    public void directedToHomePage() {
        System.out.println("user is directed to home page");
//        throw new RuntimeException("Error to check screenshot");
    }
}
