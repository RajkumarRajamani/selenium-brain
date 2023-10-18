package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.*;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;
import lombok.SneakyThrows;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

public class CucumberBaseHooks {

    private int stepIndex = 0;

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils webDriverUtils;

    @AfterStep
    public void takeScreenshotOnFailure(Scenario scenario) {
        try {
            String stepName = getStepName(scenario, stepIndex);
            if(!scenario.getStatus().equals(Status.PASSED)) {
                byte[] screenshotBytes = webDriverUtils.getScreenshotInBytes();
                scenario.attach(screenshotBytes, "image/png", "Failure Screenshot of Step : " + stepName);
            }
        } finally {
            stepIndex += 1;
        }
    }

    @SneakyThrows
    public static String getStepName(Scenario scenario, int StepIndex) {
        Field f = scenario.getClass().getDeclaredField("delegate");
        f.setAccessible(true);
        TestCaseState tcs = (TestCaseState) f.get(scenario);

        Field f2 = tcs.getClass().getDeclaredField("testCase");
        f2.setAccessible(true);
        TestCase r = (TestCase) f2.get(tcs);

        List<PickleStepTestStep> stepDefs = r.getTestSteps().stream()
                .filter(x -> x instanceof PickleStepTestStep)
                .map(x -> (PickleStepTestStep) x)
                .toList();
        PickleStepTestStep currentStepDef = stepDefs.get(StepIndex);
        return currentStepDef.getStep().getText();

    }
}