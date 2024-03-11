package org.seleniumbrain.lab.core.selenium.validator;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.core.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.core.selenium.elements.BaseElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
@ScenarioScope
public class CommonElementValidator extends ElementValidator {

    private List<Validator> validations = new LinkedList<>();

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public BaseElement baseElement;

    @Override
    public CommonElementValidator takeScreenshot(String caption) {
        Validator validation = (element, elementName) -> {
            webDriverUtils.attachScreenshot(caption);
            return ValidationResult.PASSED.name();
        };
        validations.add(validation);
        return this;
    }

    @Override
    public CommonElementValidator peek(BaseElement elementType, Consumer<BaseElement> consumer) {
        Validator validation = (element, elementName) -> {
            try {
                consumer.accept(elementType);
            } catch (Exception e){
                String error = "Unable to peek between validations. Please check the error at peek : " + e.getMessage();
                log.error(error);
                return error;
            }
            return ValidationResult.PASSED.name();
        };
        validations.add(validation);
        return this;
    }

    @Override
    public CommonElementValidator pause(long milliseconds) {
        wait.pause(milliseconds);
        return this;
    }

    @Override
    public CommonElementValidator isDisplayed() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not displayed";
            try {
                wait.untilVisibilityOf(element);
                if (element.isDisplayed()) {
                    log.info(elementName + " is displayed.");
                    return ValidationResult.PASSED.name();
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

    @Override
    public CommonElementValidator isNotDisplayed() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is displayed";
            try {
                wait.untilVisibilityOf(element);
                if (!element.isDisplayed()) {
                    log.info(elementName + " is not displayed.");
                    return ValidationResult.PASSED.name();
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

    @Override
    public CommonElementValidator isEnabled() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not enabled";
            try {
                wait.untilVisibilityOf(element);
                if (element.isEnabled()) {
                    log.info(elementName + " is enabled.");
                    return ValidationResult.PASSED.name();
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

    @Override
    public CommonElementValidator isDisabled() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not disabled";
            try {
                wait.untilVisibilityOf(element);
                if (!element.isEnabled()) {
                    log.info(elementName + " is disabled.");
                    return ValidationResult.PASSED.name();
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

    @Override
    public ValidatorOutput apply(WebElement element, String elementName) {
        try {
            Set<String> errors = new LinkedHashSet<>();
            for (Validator validation : validations) {
                String result = validation.apply(element, elementName);
                errors.add(result);
            }
            boolean areAllValidationSuccess = errors.size() == 1 && new HashSet<String>() {{
                add(ValidationResult.PASSED.name());
            }}.containsAll(errors);
            return ValidatorOutput.builder().passed(areAllValidationSuccess).errors(errors).build();
        } finally {
            validations.clear();
        }
    }

    @Override
    public ValidatorOutput applyAndAssert(WebElement element, String elementName, Assertions assertions) {
        try {
            Set<String> errors = new LinkedHashSet<>();
            for (Validator validation : validations) {
                String result = validation.apply(element, elementName);
                errors.add(result);

                if (Objects.nonNull(assertions) && !result.equalsIgnoreCase("passed")) {
                    assertions.assertFail(elementName, result);
                }
            }
            boolean areAllValidationSuccess = errors.size() == 1 && new HashSet<String>() {{
                add(ValidationResult.PASSED.name());
            }}.containsAll(errors);
            return ValidatorOutput.builder().passed(areAllValidationSuccess).errors(errors).build();
        } finally {
            validations.clear();
        }
    }
}
