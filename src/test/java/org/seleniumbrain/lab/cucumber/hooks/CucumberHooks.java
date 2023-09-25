package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.List;

public class CucumberHooks {

    private int counter = 0;

    @AfterStep
    public void afterStepHook(Scenario scenario) {
        counter++;
    }

    @SneakyThrows
    private String getStepName(Scenario scenario) {
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
        PickleStepTestStep currentStepDef = stepDefs.get(counter);
        return currentStepDef.getStep().getText();

    }
}
