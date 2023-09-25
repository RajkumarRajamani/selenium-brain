package org.seleniumbrain.lab.cucumber.spring;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = {SeleniumBrainCucumberSpringConfiguration.class, SeleniumBrainCucumberTestExecutionEngine.class})
public class SeleniumBrainCucumberSpringConfiguration {
}
