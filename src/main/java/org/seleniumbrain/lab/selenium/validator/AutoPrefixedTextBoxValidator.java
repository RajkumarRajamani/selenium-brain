package org.seleniumbrain.lab.selenium.validator;

import io.cucumber.spring.ScenarioScope;
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

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <p></p>Use this validator on text fields which shows any prefix value when value is provided in the box,
 * and disappear the prefix when value is cleared.</p>
 * <p></p>Ex., Some text field, say Limit Amount might show its currency as prefix only when the box contains value.
 * And when blank, prefix also would disappear.</p>
 *
 * <p></p>For such kind of text/input fields, use this validator.</p>
 */
@Slf4j
@Component
@ScenarioScope
public class AutoPrefixedTextBoxValidator extends ElementValidator {

    @Autowired
    private CommonElementValidator commonElementValidator;

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public TextBox textBox;

    private String defaultTextBoxPrefix = StringUtils.EMPTY;

    public AutoPrefixedTextBoxValidator withPrefix(String defaultTextBoxPrefix) {
        this.defaultTextBoxPrefix = defaultTextBoxPrefix;
        return this;
    }

    @Override
    public AutoPrefixedTextBoxValidator takeScreenshot(String caption) {
        commonElementValidator.takeScreenshot(caption);
        return this;
    }

    @Override
    public AutoPrefixedTextBoxValidator peek(BaseElement elementType, Consumer<BaseElement> consumer) {
        commonElementValidator.peek(elementType, consumer);
        return this;
    }

    @Override
    public AutoPrefixedTextBoxValidator pause(long milliseconds) {
        commonElementValidator.pause(milliseconds);
        return this;
    }

    @Override
    public AutoPrefixedTextBoxValidator isDisplayed() {
        commonElementValidator.isDisplayed();
        return this;
    }

    @Override
    public AutoPrefixedTextBoxValidator isEnabled() {
        commonElementValidator.isEnabled();
        return this;
    }

    @Override
    public AutoPrefixedTextBoxValidator isDisabled() {
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
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isEditable() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable";
            if (!isReadOnly(element)) {
                try {
                    String testText = "123";
                    String newText = textBox.setFakeAndGet(element, testText);
                    if (newText.equals(String.join("", defaultTextBoxPrefix, testText))) {
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
            } else {
                log.error(error);
                return error;
            }
        };
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * It checks if the text box contains the expected value excluding default prefix.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isMatchingWithoutPrefix(String expectedValue) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " does not contain the expected value.";
            if (!isReadOnly(element)) {
                try {
                    String elementText = textBox.getText(element).replaceFirst(defaultTextBoxPrefix, StringUtils.EMPTY);
                    if (elementText.equals(expectedValue)) {
                        log.info(elementName + " contains the expected value.");
                        return ValidationResult.PASSED.name();
                    } else {
                        log.error(error);
                        return error;
                    }
                } catch (Exception e) {
                    log.error(error);
                    return error;
                }
            } else {
                log.error(error);
                return error;
            }
        };
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * It checks if the text box contains the expected value including default prefix.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isMatchingWithPrefix(String expectedValue) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " does not contain the expected value.";
            if (!isReadOnly(element)) {
                try {
                    String elementText = textBox.getText(element);
                    if (elementText.equals(String.join("", defaultTextBoxPrefix, expectedValue))) {
                        log.info(elementName + " contains the expected value.");
                        return ValidationResult.PASSED.name();
                    } else {
                        log.error(error);
                        return error;
                    }
                } catch (Exception e) {
                    log.error(error);
                    return error;
                }
            } else {
                log.error(error);
                return error;
            }
        };
        commonElementValidator.getValidations().add(validation);
        return this;
    }

    /**
     * It checks if the text field allows entering the text of given length. It includes prefix length into account.
     *
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isTextLengthIncludingPrefixAllowedTill(int expectedAllowedLength) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is allowed to enter text beyond allowed length of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                long elementLengthIncludingPrefix = textBox.setAndGetText(element, testText).length();
                if (elementLengthIncludingPrefix == expectedAllowedLength) {
                    log.info(elementName + " is allowed to enter text max up to " + expectedAllowedLength + " chars");
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
     * It checks if the text field allows entering the text of given length. It excludes prefix length while counting length.
     *
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isTextLengthExcludingPrefixAllowedTill(int expectedAllowedLength) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is allowed to enter text beyond allowed length of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                long elementTextLengthExcludingPrefix = textBox.setAndGetText(element, testText).replaceFirst(defaultTextBoxPrefix, StringUtils.EMPTY).length();
                if (elementTextLengthExcludingPrefix == expectedAllowedLength) {
                    log.info(elementName + " is allowed to enter text max up to " + expectedAllowedLength + " chars");
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
     * <p>It checks if the text field allows to enter the text of beyond given length but show validation error element. It includes prefix length also.</p>
     *
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isTextLengthIncludingPrefixBeyondLimitShowsError(int expectedAllowedLength, WebElement uiErrorElement) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not allowed to enter text of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                long elementLengthIncludingPrefix = textBox.setAndGetText(element, testText).replaceFirst(defaultTextBoxPrefix, StringUtils.EMPTY).length();
                wait.untilVisibilityOf(uiErrorElement);
                if (elementLengthIncludingPrefix > expectedAllowedLength && uiErrorElement.isDisplayed()) {
                    log.info(elementName + " shows validation error when input text length is greater than " + expectedAllowedLength + " chars");
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
     * <p>It checks if the text field allows to enter the text of beyond given length but show validation error element. It includes prefix length also.</p>
     *
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isTextLengthExcludingPrefixBeyondLimitShowsError(int expectedAllowedLength, WebElement uiErrorElement) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not allowed to enter text of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                long elementLengthIncludingPrefix = textBox.setAndGetText(element, testText).length();
                wait.untilVisibilityOf(uiErrorElement);
                if (elementLengthIncludingPrefix > expectedAllowedLength && uiErrorElement.isDisplayed()) {
                    log.info(elementName + " shows validation error when input text length is greater than " + expectedAllowedLength + " chars");
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
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isErrorDisplayedForInvalidInputText(String invalidInputText, WebElement uiErrorElement) {
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
     * <p>It refers to a text field that accepts only number and shows some prefix when value is entered.</p>
     *
     * <p>It checks if the text box is editable and accept only numeric value.
     * Any editable numeric text box will receive only number value.
     * So, we pass a value 123xxx and if allows only numbers and discards 'xxx'
     * it assumes the field is an editable numeric field.</p>
     *
     * <p>It also considers the default prefix text while validating value.</p>
     *
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isEditableNumberField() {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable number field";
            try {
                String testText = "12xxx";
                String newText = textBox.setAndGetText(element, testText).replaceFirst(defaultTextBoxPrefix, StringUtils.EMPTY);
                if (newText.equals("12")) {
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
     * <p>It also considers its default prefix value during validation</p>
     *
     * @param numberInput      number input string value to be entered on the text field
     * @param decimalPrecision number of decimal digits
     * @param locale           region to determine the thousand-separator format
     * @return AutoPrefixedTextBoxValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isThousandSeparated(String numberInput, int decimalPrecision, Locale locale) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " value is not thousand separated.";
            try {
                String newText = textBox.setAndGetText(element, numberInput);
                String expectedValue = String.join("", defaultTextBoxPrefix, Validator.getNumberWithThousandSeparator(numberInput, decimalPrecision, locale));
                if (newText.equals(expectedValue)) {
                    log.info(elementName + " value is properly thousand separated along with prefix (" + defaultTextBoxPrefix + ")");
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
