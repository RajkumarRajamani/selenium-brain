package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.java.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.seleniumbrain.lab.core.config.SeleniumConfigReader;
import org.seleniumbrain.lab.core.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.core.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;

@Slf4j
public class CucumberHooks {

    private int stepIndex = 0;

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils driverUtil;

    @SneakyThrows
    @Before(order = 10)
    public void createBrowserSession(Scenario scenario) {
        driverFactory.initiateWebDriverSession();
    }

    private void switchLob(String scenarioId) {
        log.info("LOB is switched for " + scenarioId);
    }

    @Before(order = 11)
    public void switchLobHook(Scenario scenario) {
        String lob = getLobOfScenario(scenario);
        String scenarioId = String.join("|", lob, scenario.getId(), String.valueOf(scenario.getLine()), scenario.getName());
        while (!LobSynchronizer.getInstance().canLobBeSwitchedTo(lob, scenarioId)) {
            try {
                log.info(Thread.currentThread().getName() + " assigned for " + lob + " Test Case is waiting as other LOB case is still running in parallel thread.");
                Thread.sleep(10000);
                wait(10000);
            } catch (Exception ignored) {

            }
        }
        switchLob(scenarioId);
    }

    @AfterStep(order = 100)
    public void countStepIndex() {
        stepIndex += 1;
    }

    @After
    public void afterScenarioHook(Scenario scenario) throws MalformedURLException {
        byte[] screenshot = driverUtil.getScreenshotInBytes();
        scenario.attach(screenshot, "image/png", "step screenshot");
        driverFactory.getDriver().quit();
    }

    @After
    public void closeLobOfScenarioHook(Scenario scenario) {
        String lob = getLobOfScenario(scenario);
        String scenarioId = String.join("|", lob, scenario.getId(), String.valueOf(scenario.getLine()), scenario.getName());
        LobSynchronizer.getInstance().closeScenarioStatus(scenarioId, scenario);
    }

    @AfterAll
    public static void afterAllMethod() {
        log.info("Scenarios List:");
        String printFormat = "%-10s %-500s";
        LobSynchronizer.getInstance().getActiveScenarios().forEach((key, value) -> System.out.printf(printFormat + "%n", value, key));
    }

    private synchronized String getLobOfScenario(Scenario scenario) {
        if (!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@Terrorism")).findFirst().orElse("").isBlank()) {
            return "Terrorism";
        } else if (!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@Casualty")).findFirst().orElse("").isBlank()) {
            return "Casualty";
        } else if (!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@Prcb")).findFirst().orElse("").isBlank()) {
            return "Prcb";
        } else {
            log.info("Taking default LOB as scenario does not contain any LOB tag name ; Scenario : " + scenario.getName());
            return "Terrorism";
        }
    }
}

//        ChromeOptions browserOptions = new ChromeOptions();
//        browserOptions.setPlatformName("Windows 11");
//        browserOptions.setBrowserVersion("117");
//
//        Map<String, Object> sauceOptions = new HashMap();
//        sauceOptions.put("username", "");
//        sauceOptions.put("accessKey", "");
//        sauceOptions.put("build", "selenium-build-AH1I6");
//        sauceOptions.put("name", "saucelab-training-test");
//
//        browserOptions.setCapability("sauce:options", sauceOptions);
//
//        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
//        driver = new RemoteWebDriver(url, browserOptions);
//
