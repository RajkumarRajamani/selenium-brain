package org.seleniumbrain.lab.selenium.validator;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.BaseElement;
import org.seleniumbrain.lab.selenium.elements.Dropdown;

import static org.seleniumbrain.lab.selenium.validator.ValidationResult.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Component
@ScenarioScope
public class DropdownValidator extends ElementValidator {

    @Autowired
    private CommonElementValidator commonElementValidator;

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public Dropdown dropdown;

    @Override
    public DropdownValidator takeScreenshot(String caption) {
        commonElementValidator.takeScreenshot(caption);
        return this;
    }

    @Override
    public DropdownValidator peek(BaseElement elementType, Consumer<BaseElement> consumer) {
        commonElementValidator.peek(elementType, consumer);
        return this;
    }

    @Override
    public DropdownValidator pause(long milliseconds) {
        commonElementValidator.pause(milliseconds);
        return this;
    }

    @Override
    public DropdownValidator isDisplayed() {
        commonElementValidator.isDisplayed();
        return this;
    }

    @Override
    public DropdownValidator isNotDisplayed() {
        commonElementValidator.isNotDisplayed();
        return this;
    }

    @Override
    public DropdownValidator isEnabled() {
        commonElementValidator.isEnabled();
        return this;
    }

    @Override
    public DropdownValidator isDisabled() {
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

    /**
     * <p>Checks if the given element is a dropdown or not.</p>
     *
     * @return {@link DropdownValidator} DropdownValidator this class object itself
     */
    public DropdownValidator isDropdown() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not a dropdown.";
            try {
                if (element.getTagName().equals("select")) {
                    log.info(elementName + " is a dropdown element.");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * <p>Checks if the given dropdown element is multi-select or not.</p>
     *
     * @return {@link DropdownValidator} DropdownValidator this class object itself
     */
    public DropdownValidator isMultiSelect() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not a multi-select dropdown.";
            try {
                if (new Select(element).isMultiple()) {
                    log.info(elementName + " is a multi-select dropdown");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * <p>Checks if the given dropdown element contains the desired option.</p>
     *
     * @return {@link DropdownValidator} DropdownValidator this class object itself
     */
    public DropdownValidator isOptionAvailable(String option) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " does not contain the given value '" + option + "' as one of its option.";
            try {
                if (dropdown.getOptions(element).contains(option)) {
                    log.info(elementName + " contains the given option '" + option + "' as one of its option.");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * <p>Checks if the given dropdown element contains the all desired options.</p>
     *
     * @return {@link DropdownValidator} DropdownValidator this class object itself
     */
    public DropdownValidator isOptionsAvailable(List<String> options) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " does not contain the given list of options.";
            try {
                if (dropdown.getOptions(element).containsAll(options)) {
                    log.info(elementName + " contains all the given options.");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    public DropdownValidator isSelectedWith(String option) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not selected with given option '" + option + "'.";
            try {
                if (dropdown.getSelectedValue(element).equals(option)) {
                    log.info(elementName + " is selected with given value '" + option + "'.");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    public DropdownValidator isSelectedWith(Set<String> options) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not selected with given options";
            try {
                if (dropdown.getOptions(element).containsAll(options)) {
                    log.info(elementName + " is selected with all the given options");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    public DropdownValidator isBlank() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not blank.";
            try {
                if (dropdown.getSelectedValue(element).isBlank()) {
                    log.info(elementName + " is blank");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    public DropdownValidator isErrorDisplayedForDefaultPlaceholder(WebElement errorElement, String defaultPlaceholder) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not selected with given options";
            try {
                if (dropdown.getSelectedValue(element).equals(defaultPlaceholder)
                        &&
                        this.isDisplayed().apply(errorElement, elementName + " Error Element").isPassed()) {
                    log.info(elementName + " shows an validation error element on selecting invalid option");
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
        commonElementValidator.getValidations().add(validation);
        return this;
    }

}
