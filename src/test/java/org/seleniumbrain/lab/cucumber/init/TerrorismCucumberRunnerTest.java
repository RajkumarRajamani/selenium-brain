package org.seleniumbrain.lab.cucumber.init;

import io.cucumber.testng.*;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        plugin = {
                "pretty",
                "html:test-output/cucumber/cucumber-report-terrorism.html",
                "json:test-output/cucumber/cucumber-report-terrorism.json",
                "timeline:test-output/cucumber/cucumber-parallel-run-thread-terrorism-report.html",
                "org.seleniumbrain.lab.easyreport.core.EasyReportJsonFormatter:test-output/easy-cucumber-report/easy-cucumber-report-terrorism.html"
        },
        features = {"src/test/resources/cucumber/feature-files"},
        glue = {
                "org.seleniumbrain.lab.core.cucumber.spring",
                "org.seleniumbrain.lab.core.cucumber.hooks",
                "org.seleniumbrain.lab.cucumber.hooks",
                "org.seleniumbrain.lab.cucumber.init",
                "org.seleniumbrain.lab.cucumber.stepdefinitions"
        },
        monochrome = true,
        tags = "@Terrorism"
)

public class TerrorismCucumberRunnerTest extends AbstractTestNGCucumberTests {

        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

}

