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
import org.seleniumbrain.lab.core.selenium.elements.Collapsible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
@ScenarioScope
public class CollapsibleValidator extends ElementValidator {

    @Autowired
    private CommonElementValidator commonElementValidator;

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public Collapsible collapsible;

    @Override
    public CollapsibleValidator takeScreenshot(String caption) {
        commonElementValidator.takeScreenshot(caption);
        return this;
    }

    @Override
    public CollapsibleValidator peek(BaseElement elementType, Consumer<BaseElement> consumer) {
        commonElementValidator.peek(elementType, consumer);
        return this;
    }

    @Override
    public CollapsibleValidator pause(long milliseconds) {
        commonElementValidator.pause(milliseconds);
        return this;
    }

    @Override
    public CollapsibleValidator isDisplayed() {
        commonElementValidator.isDisplayed();
        return this;
    }

    @Override
    public CollapsibleValidator isNotDisplayed() {
        commonElementValidator.isNotDisplayed();
        return this;
    }

    @Override
    public CollapsibleValidator isEnabled() {
        commonElementValidator.isEnabled();
        return this;
    }

    @Override
    public CollapsibleValidator isDisabled() {
        commonElementValidator.isDisabled();
        return this;
    }

    @Override
    public ValidatorOutput apply(WebElement element, String elementName) {
        return commonElementValidator.apply(element, elementName);
    }

    @Override
    public ValidatorOutput applyAndAssert(WebElement element, String elementName, Assertions assertions) {
        return commonElementValidator.applyAndAssert(element, elementName, assertions);
    }

    public CollapsibleValidator isAriaCollapsed() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not collapsed.";
            try {
                if(collapsible.isAriaCollapsed(element)) {
                    log.info(elementName + " is collapsed.");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    public CollapsibleValidator isAriaExpanded() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not expanded.";
            try {
                if(collapsible.isAriaCollapsed(element)) {
                    log.info(elementName + " is expanded.");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }
}
