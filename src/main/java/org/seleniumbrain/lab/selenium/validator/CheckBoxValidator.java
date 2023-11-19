package org.seleniumbrain.lab.selenium.validator;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.BaseElement;
import org.seleniumbrain.lab.selenium.elements.Checkbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
@ScenarioScope
public class CheckBoxValidator extends ElementValidator {

    @Autowired
    private CommonElementValidator commonElementValidator;

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public Checkbox checkbox;

    @Override
    public CheckBoxValidator takeScreenshot(String caption) {
        commonElementValidator.takeScreenshot(caption);
        return this;
    }

    @Override
    public CheckBoxValidator peek(BaseElement elementType, Consumer<BaseElement> consumer) {
        commonElementValidator.peek(elementType, consumer);
        return this;
    }

    @Override
    public CheckBoxValidator pause(long milliseconds) {
        commonElementValidator.pause(milliseconds);
        return this;
    }

    @Override
    public CheckBoxValidator isDisplayed() {
        commonElementValidator.isDisplayed();
        return this;
    }

    @Override
    public CheckBoxValidator isEnabled() {
        commonElementValidator.isEnabled();
        return this;
    }

    @Override
    public CheckBoxValidator isDisabled() {
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

    public CheckBoxValidator isChecked() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not checked.";
            try {
                return null;
            } catch (Exception e) {
                log.error(error);
                return error;
            }
        };
        commonElementValidator.getValidations().add(validation);
        return this;
    }


}
