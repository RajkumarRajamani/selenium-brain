package org.seleniumbrain.lab.cucumber.init;

import io.cucumber.testng.*;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CucumberOptions(
        plugin = {
                "pretty",
                "html:test-output/cucumber/cucumber-report.html",
                "json:test-output/cucumber/cucumber-report.json",
                "timeline:test-output/cucumber/cucumber-parallel-run-thread-report.html",
                "org.seleniumbrain.lab.easyreport.core.EasyReportJsonFormatter:test-output/easy-cucumber-report/easy-cucumber-report.html"
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
        tags = "@Test-Feature"
)

public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

}

