package org.seleniumbrain.lab.core.selenium.validator;

import org.openqa.selenium.WebElement;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.core.selenium.elements.BaseElement;

import java.util.function.Consumer;

public abstract class ElementValidator {

    /**
     * <p>Used to take screenshot while making multiple validations on an element</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator takeScreenshot(String caption);

    /**
     * <p>Used to add additional work load on element of type {@code BaseElement}</p>
     *
     * <p>While chaining multiple validations on an element,
     * we can perform some usual operations on an element instead of validation</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator peek(BaseElement elementType, Consumer<BaseElement> consumer);

    /**
     * <p>use this to pause the execution for a while between validation</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator pause(long seconds);

    /**
     * <p>Checks if an element is displayed on current page</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator isDisplayed();

    /**
     * <p>Checks if an element is not displayed on current page</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator isNotDisplayed();

    /**
     * <p>Checks if an element is enabled on current page</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator isEnabled();

    /**
     * <p>Checks if an element is disabled on current page</p>
     *
     * @return ElementValidator this class object itself
     */
    public abstract ElementValidator isDisabled();

    /**
     * <p>It performs each validations in its sequence of added them. It returns the validation
     * result as {@link ValidatorOutput} containing</p>
     * <ul>
     * <li>{@code boolean} passed - returns if validations are passed</li>
     * <li>{@code Set<String} errors - returns collection of errors</li>
     * <li>{@code getErrorsAsText} method - returns collection of errors in String format</li>
     * </ul>
     *
     * @return ValidatorOutput this class object itself
     */
    public abstract ValidatorOutput apply(WebElement element, String elementName);

    /**
     * <p>It performs all validations chained in its order and add it to the given {@code Assertion} object.
     * You can call {@code assertAll()} method at any time. Also it returns the validation output
     * in the form of {@link ValidatorOutput}
     * </p>
     *
     * @param element     {@link WebElement} element to perform the validations on
     * @param elementName name of the element for better logging
     * @param assertions  {@link Assertions} to add all validations chained in its order
     * @return ValidatorOutput this class object itself
     */
    public abstract ValidatorOutput applyAndAssert(WebElement element, String elementName, Assertions assertions);

}
