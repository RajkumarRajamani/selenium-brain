package org.seleniumbrain.lab.snippet.pagerepositories.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.SharedStateKey;
import org.seleniumbrain.lab.core.selenium.elements.TextBox;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.PageObjects;
import org.seleniumbrain.lab.core.selenium.validator.ElementValidator;
import org.seleniumbrain.lab.utility.FileUtils;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;
import org.seleniumbrain.lab.snippet.BaseObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@PageObjects
public class SwagLabLoginPageOR extends BaseObjectRepository {

    @FindBy(id = "user-name")
    private WebElement userName;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "login-button")
    private WebElement login;

    @Autowired
    private FileUtils fileUtils;

    public void assertB() {
        System.out.println("Login Page : " + Objects.isNull(assertions));
        assertions.assertEqualsTo("TestLabel 2", 1, 1, "1 != 1", "1 == 1");
    }

    public SwagLabLoginPageOR enterUserName(String userName) {
        textBox.setText(this.getUserName(), "INR ");
        textBox.setFakeAndGet(this.getUserName(), "fake user name");
        scenarioState.getCacheText().put(SharedStateKey.NAME, "Enter User Name step");

        ElementValidator validator = textBoxValidator
                .isDisplayed()
                .isEnabled()
                .isEditable()
                .takeScreenshot("prefixed input")
                .peek(textBox, txtBox -> ((TextBox) txtBox).setText(this.getUserName(), "Consumer Text"))
                .takeScreenshot("Consumer peek1")
                .peek(textBox, txtBox -> ((TextBox) txtBox).setText(this.getUserName(), "Consumer Re-Entered Text"))
                .takeScreenshot("Consumer peek2")
                .pause(2000)
                .isThousandSeparated("1234423.23", 2, Locale.US)
                .takeScreenshot("thousand separator")
                .isTextLengthAllowedTill(15)
                .takeScreenshot("username above 15 chars");

        validator.applyAndAssert(this.getUserName(), "UserName Field", assertions);
        textBox.setText(this.getUserName(), userName);

        String json = JsonBuilder.getObjectBuilder().withEmptyNode()
                .append("key", "value")
                .build().toPrettyString();
        fileUtils.writeFileAsJson(getFileNameForScenario("demo-file"), json);
        return this;
    }

    public SwagLabLoginPageOR enterPassword(String password) {
        textBoxValidator
                .isDisplayed()
                .isEnabled()
                .isEditable()
                .isTextLengthAllowedTill(6)
                .takeScreenshot("password")
                .applyAndAssert(this.getPassword(), "Password Field", assertions);
        textBox.setText(this.getPassword(), password);
        return this;
    }

    public SwagLabHomePageOR login() {
        this.getLogin().click();
        return swagLabHomePageOR.withAssertion(assertions, swagLabHomePageOR);
    }

}
