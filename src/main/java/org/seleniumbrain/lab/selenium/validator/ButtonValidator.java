package org.seleniumbrain.lab.selenium.validator;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.Button;
import org.seleniumbrain.lab.selenium.elements.TextBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.seleniumbrain.lab.selenium.validator.ValidatorResult.PASSED;

@Slf4j
@Component
@ScenarioScope
public class ButtonValidator {

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public Button button;

    protected List<Validator> validations = new LinkedList<>();

    private Assertions assertions;

    public ButtonValidator withAssertion(Assertions assertions) {
        this.assertions = assertions;
        return this;
    }

    public ButtonValidator takeScreenshot(String caption) {
        Validator validation = (element, elementName) -> {
            webDriverUtils.attachScreenshot(caption);
            return PASSED.name();
        };
        validations.add(validation);
        return this;
    }

    /**
     * It checks if the button is displayed on the web page.
     *
     * @return ButtonValidator this class object itself
     */
    public ButtonValidator isDisplayed() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not displayed";
            try {
                wait.untilVisibilityOf(element);
                if (element.isDisplayed()) {
                    log.info(elementName + " is displayed.");
                    return PASSED.name();
                } else {
                    log.error(error);
                    return error;
                }
            } catch (Exception e) {
                log.error(error);
                return error;
            }
        };
        validations.add(validation);
        return this;
    }

    /**
     * It checks if the button is enabled.
     *
     * @return ButtonValidator this class object itself
     */
    public ButtonValidator isEnabled() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not enabled";
            try {
                if (element.isEnabled()) {
                    log.info(elementName + " is enabled.");
                    return PASSED.name();
                } else {
                    log.error(error);
                    return error;
                }
            } catch (Exception e) {
                log.error(error);
                return error;
            }
        };
        validations.add(validation);
        return this;
    }

    /**
     * It checks if the button is disabled.
     *
     * @return ButtonValidator this class object itself
     */
    public ButtonValidator isDisabled() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not disabled";
            try {
                if (!element.isEnabled()) {
                    log.info(elementName + " is disabled.");
                    return PASSED.name();
                } else {
                    log.error(error);
                    return error;
                }
            } catch (Exception e) {
                log.error(error);
                return error;
            }
        };
        validations.add(validation);
        return this;
    }

    public Set<String> apply(WebElement element, String elementName) {
        try {
            Set<String> errors = new LinkedHashSet<>();
            for (Validator validation : validations) {
                String result = validation.apply(element, elementName);
                errors.add(result);

                if (Objects.nonNull(assertions) && !result.equalsIgnoreCase("passed")) {
                    assertions.assertFail(elementName, result);
                }
            }
            return errors;
        } finally {
            validations.clear();
        }
    }

}
