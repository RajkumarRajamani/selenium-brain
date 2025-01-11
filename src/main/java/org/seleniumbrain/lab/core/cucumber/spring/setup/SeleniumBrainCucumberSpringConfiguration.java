package org.seleniumbrain.lab.core.cucumber.spring.setup;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Configuration class for setting up Cucumber with Spring Boot.
 * This class integrates Cucumber with the Spring TestContext Framework.
 *
 * <p>It uses {@link CucumberContextConfiguration} to configure the Cucumber context
 * and {@link SpringBootTest} to load the Spring Boot application context.</p>
 *
 * @author Rajkumar Rajamani
 * @version 1.0
 * @since 2024-03-01
 */
@CucumberContextConfiguration
@SpringBootTest(classes = {SeleniumBrainCucumberSpringConfiguration.class, SeleniumBrainCucumberTestExecutionEngine.class})
public class SeleniumBrainCucumberSpringConfiguration {
}
