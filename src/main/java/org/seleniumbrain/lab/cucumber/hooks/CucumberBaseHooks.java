package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.*;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.cucumber.spring.configure.CucumberStepLog;
import org.seleniumbrain.lab.cucumber.spring.configure.ScenarioState;
import org.seleniumbrain.lab.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.pageobjectmodel.SharedStateKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CucumberBaseHooks {

    /**
     * Used to refer the index of the current gherkin step being executed.
     * Increasing its count for every step. As hooks objects are created for every cucumber scenario internally,
     * this index starts with 0 at the beginning of every scenario.
     */
    private int stepIndex = 0;

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils webDriverUtils;

    @Autowired
    private ScenarioState scenarioState;

    /**
     * @implNote Generates a prefix text based on the current feature file and a scenario being executed.
     * We can sue this text as a prefix of a file name for any file that is generated during execution.
     *
     * @implSpec To generate proper prefix text, it is recommended to add every cucumber feature file with
     * <code>@FID-featureId</code> and every cucumber scenario as <code>@SID-scenarioId</code>
     *
     * @param scenario current cucumber scenario being executed
     */
    @Before(order = 1)
    public void generateFileNamePrefix(Scenario scenario) {
        try {
            log.info(StringUtils.repeat("=", 50) + "Starts Scenario(Line#" + scenario.getLine() + ") : " + scenario.getName());
            String featureId = scenario.getSourceTagNames().stream().filter(tag -> tag.startsWith("@FID")).findFirst().orElse("FID").replace("@FID-", "").replace("FID", "");
            String scenarioId = scenario.getSourceTagNames().stream().filter(tag -> tag.startsWith("@SID")).findFirst().orElse("SID").replace("@SID-", "").replace("SID", "");
            scenarioState.getCacheText().put(
                    SharedStateKey.SCENARIO_NAME_PREFIX,
                    String.join(File.separator, featureId, featureId + "_" + scenarioId + "_Line-" + scenario.getLine() + "_")
                    );
            log.info("Any test evidences generated for this scenario can have its prefix name as '" + scenarioState.getCacheText().get(SharedStateKey.SCENARIO_NAME_PREFIX));
        } catch (Exception e) {
            log.error("Error while generating ScenarioNamePrefix." + e.getMessage());
        }
    }


    /**
     * It attaches the screenshots or text log for every cucumber step to provide evidence of test result.
     * In case of failure, screenshot will be taken as mandatory.
     * Otherwise, unless intentionally called to add screenshot as part of step-definition, it wouldn't take screenshot.
     *
     * @param scenario current cucumber scenario being executed
     */
    @AfterStep(order = 100)
    public void attachScreenshotsOfStep(Scenario scenario) {
        String stepName = getStepName(scenario, stepIndex);
        try {
            List<CucumberStepLog> stepLogs = scenarioState.getStepLogs();
            if (!stepLogs.isEmpty()) {
                int action = 1;
                for (CucumberStepLog log : stepLogs) {
                    if (Objects.nonNull(log.getImg()) && log.getImg().length != 0) {
                        scenario.attach(log.getImg(), "image/png", "Step " + (stepIndex + 1) + "- Action " + action + " : " + log.getCaption());
                    }

                    if (Objects.nonNull(log.getTextLog()) && !log.getTextLog().isBlank()) {
                        scenario.attach(log.getTextLog(), "text/plain", "Step " + (stepIndex + 1) + "- Action " + action + " : " + log.getCaption());
                    }

                    action++;
                }
            }
        } finally {
            scenarioState.getStepLogs().clear();
            try {
                if (!scenario.getStatus().equals(Status.PASSED)) {
                    byte[] screenshotBytes = webDriverUtils.getScreenshotInBytes();
                    scenario.attach(screenshotBytes, "image/png", "Failure Screenshot of Step : " + stepName);
                }
            } finally {
                stepIndex += 1;
            }
        }
    }

    /**
     * @param scenario  current cucumber scenario being executed
     * @param StepIndex sequence number of gherkin step of current cucumber scenario being executed
     * @return name of the step being executed
     */
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