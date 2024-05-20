package org.seleniumbrain.lab.cucumber.init;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        plugin = {
                "pretty",
                "html:test-output/cucumber/cucumber-report-prcb.html",
                "json:test-output/cucumber/cucumber-report-prcb.json",
                "timeline:test-output/cucumber/cucumber-parallel-run-thread-prcb-report.html",
                "org.seleniumbrain.lab.easyreport.core.EasyReportJsonFormatter:test-output/easy-cucumber-report/easy-cucumber-report-prcb.html"
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
        tags = "@Prcb"
)

public class PrcbCucumberRunnerTest extends AbstractTestNGCucumberTests {

        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

}

