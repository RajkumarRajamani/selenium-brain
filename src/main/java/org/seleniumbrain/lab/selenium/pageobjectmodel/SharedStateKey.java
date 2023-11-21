package org.seleniumbrain.lab.selenium.pageobjectmodel;

public enum SharedStateKey {

    /**
     * @implNote This Enum Key is supposed to be used to store prefix name of the test being executed.
     *
     * <ul>
     *     <li>In the case of a cucumber framework, use cucumber hook {@code Before} methods to derive the prefix text using a test scenario tag/line-number</li>
     *     <li>In the case of testng framework, use {@code Listener} methods to derive the prefix using a test case sequence/order/method-name</li>
     * </ul>
     *
     * Do not delete this Enum key
     */
    SCENARIO_NAME_PREFIX,

    NAME;
}
