package org.seleniumbrain.lab.cucumber.init;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        plugin = {
                "pretty",
                "html:test-output/cucumber/cucumber-report.html",
                "json:test-output/cucumber/cucumber-report.json",
                "timeline:test-output/cucumber/cucumber-parallel-run-thread-report.html"
        },
        features = {"src/test/resources/cucumber/feature-files"},
        glue = {
                "org.seleniumbrain.lab.cucumber.hooks.CucumberBaseHooks",
                "org.seleniumbrain.lab.cucumber.hooks",
                "org.seleniumbrain.lab.cucumber.init",
                "org.seleniumbrain.lab.cucumber.stepdefinitions",
                "org.seleniumbrain.lab.cucumber.spring"
        },
        monochrome = true,
        tags = "@Test-Feature"
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }
}

