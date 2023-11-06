package org.seleniumbrain.lab.selenium.validator;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.TextBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

import static org.seleniumbrain.lab.selenium.validator.ValidatorResult.PASSED;

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
public class AutoPrefixedTextBoxValidator {

    @Autowired
    public WebDriverWaits wait;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public TextBox textBox;

    protected List<Validator> validations = new LinkedList<>();

    private Assertions assertions;

    private String defaultTextBoxPrefix = StringUtils.EMPTY;

    public AutoPrefixedTextBoxValidator withAssertion(Assertions assertions) {
        this.assertions = assertions;
        return this;
    }

    public AutoPrefixedTextBoxValidator withPrefix(String defaultTextBoxPrefix) {
        this.defaultTextBoxPrefix = defaultTextBoxPrefix;
        return this;
    }

    public AutoPrefixedTextBoxValidator pause(long seconds) {
        wait.pause(seconds);
        return this;
    }

    public AutoPrefixedTextBoxValidator takeScreenshot(String caption) {
        Validator validation = (element, elementName) -> {
            webDriverUtils.attachScreenshot(caption);
            return PASSED.name();
        };
        validations.add(validation);
        return this;
    }

    /**
     * It checks if the text box is displayed on web page.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isDisplayed() {
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
     * It checks if the text box is enabled.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isEnabled() {
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
     * It checks if the text box is disabled.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isDisabled() {
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

    private boolean isReadOnly(WebElement element) {
        String readOnly = element.getAttribute("readonly");
        return Objects.nonNull(readOnly) && readOnly.equalsIgnoreCase("true");
    }

    /**
     * It checks if the text box is editable field.
     * Any editable text box will receive any value.
     * So, we pass a value 1 and if it is entered successfully,
     * it assumes the field is an editable one.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isEditable() {
//        if (newText.replaceAll("[^\\d]", "").trim().equals(testText)) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable";
            if (!isReadOnly(element)) {
                try {
                    String testText = "123";
                    String newText = textBox.setFakeAndGet(element, testText);
                    if (newText.equals(String.join("", defaultTextBoxPrefix, testText))) {
                        log.info(elementName + " is editable.");
                        return PASSED.name();
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
        validations.add(validation);
        return this;
    }

    /**
     * It checks if the text field allows to enter the text of given length. It includes prefix length into account.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isTextLengthIncludingPrefixAllowedTill(int expectedAllowedLength) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is allowed to enter text beyond allowed length of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                long elementLengthIncludingPrefix = textBox.setAndGetText(element, testText).length();
                if (elementLengthIncludingPrefix == expectedAllowedLength) {
                    log.info(elementName + " is allowed to enter text max up to " + expectedAllowedLength + " chars");
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
     * It checks if the text field allows to enter the text of given length. It excludes prefix length while counting length.
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isTextLengthExcludingPrefixAllowedTill(int expectedAllowedLength) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is allowed to enter text beyond allowed length of " + expectedAllowedLength + " chars.";
            try {
                String testText = new String(new char[expectedAllowedLength]).replaceAll(".", "test-text").substring(0, expectedAllowedLength + 1);
                long elementTextLengthExcludingPrefix = textBox.setAndGetText(element, testText).replaceFirst(defaultTextBoxPrefix, StringUtils.EMPTY).length();
                if (elementTextLengthExcludingPrefix == expectedAllowedLength) {
                    log.info(elementName + " is allowed to enter text max up to " + expectedAllowedLength + " chars");
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
     * <p>It checks if the text field allows to enter the text of beyond given length but show validation error element. It includes prefix length also.</p>
     *
     * @return TextValidator this class object itself
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
     * <p>It checks if the text field allows to enter the text of beyond given length but show validation error element. It includes prefix length also.</p>
     *
     * @return TextValidator this class object itself
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
     * It checks if the text field shows validation error on UI for an invalid input text value
     *
     * @return TextValidator this class object itself
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
     * <p>It refers to a text field that accepts only number and shows some prefix when value is entered.</p>
     *
     * <p>It checks if the text box is editable and accept only numeric value.
     * Any editable numeric text box will receive only number value.
     * So, we pass a value 123xxx and if allows only numbers and discards 'xxx'
     * it assumes the field is an editable numeric field.</p>
     *
     * <p>It also considers the default prefix text while validating value.</p>
     *
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isEditableNumberField() {
//        if (newText.replaceAll("[^\\dxxx]", "").trim().equals("123")) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " is not editable number field";
            try {
                String testText = "12xxx";
                String newText = textBox.setAndGetText(element, testText).replaceFirst(defaultTextBoxPrefix, StringUtils.EMPTY);
                if (newText.equals("12")) {
                    log.info(elementName + " is editable number field.");
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
     * <p>It checks if the text field, allowing only numbers, shows its value with a thousand separator as per Locale</p>
     *
     * <p>It also considers its default prefix value during validation</p>
     *
     * @param numberInput      number input string value to be entered on the text field
     * @param decimalPrecision number of decimal digits
     * @param locale           region to determine the thousand-separator format
     * @return TextValidator this class object itself
     */
    public AutoPrefixedTextBoxValidator isThousandSeparated(String numberInput, int decimalPrecision, Locale locale) {
        Validator validation = (element, elementName) -> {
            String error = elementName + " value is not thousand separated.";
            try {
                String newText = textBox.setAndGetText(element, numberInput);
                String expectedValue = String.join("", defaultTextBoxPrefix, Validator.getNumberWithThousandSeparator(numberInput, decimalPrecision, locale));
                if (newText.equals(expectedValue)) {
                    log.info(elementName + " value is properly thousand separated along with prefix (" + defaultTextBoxPrefix + ")");
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

    public static void main(String[] args) {
        AutoPrefixedTextBoxValidator validator = new AutoPrefixedTextBoxValidator();
        System.out.println(validator.isEditable().isEnabled().isDisabled().isEditable().isEnabled().apply(null, null));
        System.out.println(Validator.getNumberWithThousandSeparator("23333333.55658", 3, Locale.US));

        double val = 232323.53232;
        DecimalFormat format = new DecimalFormat("0");
        format.setMaximumFractionDigits(0);
        System.out.println(format.format(val));

        test();
    }

    public static void test() {
        String plainText = "helloworld";
        String key = "rajkumar#";
        int repeat = 4;
        int remainder = plainText.length() % key.length();

        String result = new String(new char[repeat]).replaceAll(".", key).substring(0, repeat);
        System.out.println(result);
        System.out.println(result.length());
    }
}
