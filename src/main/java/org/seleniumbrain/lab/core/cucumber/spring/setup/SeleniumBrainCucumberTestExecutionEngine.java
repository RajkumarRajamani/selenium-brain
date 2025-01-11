package org.seleniumbrain.lab.core.cucumber.spring.setup;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class for executing Cucumber tests with Spring Boot.
 * This class sets up the Spring Boot application context and component scanning.
 *
 * <p>It uses {@link SpringBootApplication} to enable Spring Boot's auto-configuration
 * and {@link ComponentScan} to specify the base packages to scan for Spring components.</p>
 *
 * @author Rajkumar Rajamani
 * @version 1.0
 * @since 2024-03-01
 */
@SpringBootApplication
@ComponentScan("org.seleniumbrain")
public class SeleniumBrainCucumberTestExecutionEngine {
}
