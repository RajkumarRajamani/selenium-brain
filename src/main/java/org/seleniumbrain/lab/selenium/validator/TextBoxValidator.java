package org.seleniumbrain.lab.selenium.validator;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.BaseElement;
import org.seleniumbrain.lab.selenium.elements.TextBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
@ScenarioScope
public class TextBoxValidator extends ElementValidator {

    @Autowired
    private CommonElementValidator commonElementValidator;

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public TextBox textBox;

    @Override
    public TextBoxValidator takeScreenshot(String caption) {
        commonElementValidator.takeScreenshot(caption);
        return this;
    }

    @Override
    public TextBoxValidator peek(BaseElement elementType, Consumer<BaseElement> consumer) {
        commonElementValidator.peek(elementType, consumer);
        return this;
    }

    @Override
    public TextBoxValidator pause(long milliseconds) {
        commonElementValidator.pause(milliseconds);
        return this;
    }

    @Override
    public TextBoxValidator isDisplayed() {
        commonElementValidator.isDisplayed();
        return this;
    }

    @Override
    public TextBoxValidator isNotDisplayed() {
        commonElementValidator.isNotDisplayed();
        return this;
    }

    @Override
    public TextBoxValidator isEnabled() {
        commonElementValidator.isEnabled();
        return this;
    }

    @Override
    public TextBoxValidator isDisabled() {
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

    private boolean isReadOnly(WebElement element) {
        String readOnly = element.getAttribute("readonly");
        return Objects.nonNull(readOnly) && readOnly.equalsIgnoreCase("true");
    }

    /**
     * It checks if the text box is editable field.
     * Any editable text box will receive any value.
     * So, we pass value 1 and if it is entered successfully,
     * it assumes the field is editable.
     *
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isEditable() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable";
            try {
                String testText = "1";
                String newText = textBox.setFakeAndGet(element, testText);
                if (newText.equals(testText)) {
                    log.info(elementName + " is editable.");
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

    /**
     * It checks if the text-box/text-label is not editable field.
     * Any non-editable field will throw {@link org.openqa.selenium.ElementNotInteractableException} while using sendKeys() method.
     * This will result if a field is editable or not.
     *
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public TextBoxValidator isNotEditable() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable";
            try {
                element.sendKeys("1");
                if (textBox.getText(element).equals("1")) {
                    log.info(error);
                    return error;
                } else {
                    log.error(elementName + " is not editable");
                    return ValidationResult.PASSED.name();
                }
            } catch (Exception e) {
                log.error(elementName + " is not editable");
                return ValidationResult.PASSED.name();
            }
        };
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * It checks if the text box contains the expected value.
     *
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isMatching(String expectedValue) {
        Validator validation = (element, elementName) -> {
            String error = "{0} does not contain the expected value ({1}). Instead it contains ({2}).";
            String elementText = null;
            try {
                elementText = textBox.getText(element);
                if (elementText.equals(expectedValue)) {
                    log.info(MessageFormat.format("{0} contains the expected value ({1})", elementName, expectedValue));
                    return ValidationResult.PASSED.name();
                } else {
                    log.error(MessageFormat.format(error, elementName, expectedValue, elementText));
                    return MessageFormat.format(error, elementName, expectedValue, elementText);
                }
            } catch (Exception e) {
                log.error(MessageFormat.format(error, elementName, expectedValue, elementText));
                return MessageFormat.format(error, elementName, expectedValue, elementText);
            }
        };
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * It checks if the text field allows entering the text of given length.
     *
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isTextLengthAllowedTill(int expectedAllowedLength) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is allowed to enter text beyond allowed length of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                textBox.setText(element, testText);
                long actualAllowedLength = textBox.getTextLength(element);
                if (actualAllowedLength == expectedAllowedLength) {
                    log.info(elementName + " is allowed to enter text max up to " + expectedAllowedLength + " chars.");
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

    /**
     * It checks if the text field allows entering the text of beyond given length but showing a validation error element.
     *
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isTextLengthBeyondLimitShowsError(int expectedAllowedLength, WebElement uiErrorElement) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not allowed to enter text of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                textBox.setText(element, testText);
                long length = textBox.getTextLength(element);
                wait.untilVisibilityOf(uiErrorElement);
                if (length > expectedAllowedLength && uiErrorElement.isDisplayed()) {
                    log.info(elementName + " shows validation error when input text length is greater than " + expectedAllowedLength + " chars.");
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

    /**
     * It checks if the text field shows validation error on UI for an invalid input text value
     *
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isErrorDisplayedForInvalidInputText(String invalidInputText, WebElement uiErrorElement) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not showing validation error for invalid input text '" + invalidInputText + "'";
            try {
                textBox.setText(element, invalidInputText);
                String elementText = textBox.getText(element);
                wait.untilVisibilityOf(uiErrorElement);
                if (elementText.equals(invalidInputText) && uiErrorElement.isDisplayed()) {
                    log.info(elementName + " shows validation error for invalid input text '" + invalidInputText + "'");
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


    /**
     * It checks if the text box is editable and accept only numeric value.
     * Any editable numeric text box will receive only number value.
     * So, we pass a value 123xxx and if it allows only numbers and discards 'xxx'.
     * It assumes the field is an editable numeric field.
     *
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isEditableNumberField() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable number field";
            try {
                String testText = "1xxx";
                String newText = textBox.setAndGetText(element, testText);
                if (newText.equals("1")) {
                    log.info(elementName + " is editable number field.");
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

    /**
     * <p>It checks if the text field, allowing only numbers, shows its value with a thousand separator as per Locale</p>
     *
     * @param numberInput      number input string value to be entered on the text field
     * @param decimalPrecision number of decimal digits
     * @param locale           region to determine the thousand-separator format
     * @return TextValidator this class object itself
     */
    public TextBoxValidator isThousandSeparated(String numberInput, int decimalPrecision, Locale locale) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " value is not thousand separated.";
            try {
                String newText = textBox.setAndGetText(element, numberInput);
                String expectedValue = Validator.getNumberWithThousandSeparator(numberInput, decimalPrecision, locale);
                if (newText.equals(expectedValue)) {
                    log.info(elementName + " value is properly thousand separated");
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
