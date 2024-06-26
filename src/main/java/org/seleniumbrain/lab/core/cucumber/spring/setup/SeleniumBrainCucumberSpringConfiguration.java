package org.seleniumbrain.lab.core.cucumber.spring.setup;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = {SeleniumBrainCucumberSpringConfiguration.class, SeleniumBrainCucumberTestExecutionEngine.class})
public class SeleniumBrainCucumberSpringConfiguration {
}
