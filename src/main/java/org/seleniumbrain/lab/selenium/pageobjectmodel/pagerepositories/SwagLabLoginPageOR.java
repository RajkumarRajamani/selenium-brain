package org.seleniumbrain.lab.selenium.pageobjectmodel.pagerepositories;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.seleniumbrain.lab.selenium.pageobjectmodel.spring.PageObjects;

import java.util.Locale;

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

    public SwagLabLoginPageOR enterUserName(String userName) {
        getTextBox().setText(this.getUserName(), "INR ");
        getTextBox().setFakeAndGet(this.getUserName(), "fake user name");
        getTextBoxValidator()
                .withAssertion(getAssertions())
                .isDisplayed()
                .isEnabled()
                .isEditable()
                .takeScreenshot("prefixed input")
                .pause(2000)
                .isThousandSeparated("1234423.23", 2, Locale.US)
                .takeScreenshot("thousand separator")
                .isTextLengthAllowedTill(15)
                .takeScreenshot("username above 15 chars")
                .apply(this.getUserName(), "UserName Field");
        getTextBox().setText(this.getUserName(), userName);
        return this;
    }

    public SwagLabLoginPageOR enterPassword(String password) {
        getTextBoxValidator()
                .withAssertion(getAssertions())
                .isDisplayed()
                .isEnabled()
                .isEditable()
                .isTextLengthAllowedTill(6)
                .takeScreenshot("password")
                .apply(this.getPassword(), "Password Field");
        getTextBox().setText(this.getPassword(), password);
        return this;
    }

    public SwagLabHomePageOR login() {
        this.getLogin().click();
        return getSwagLabHomePageOR().withAssertion(getAssertions(), getSwagLabHomePageOR());
    }

}
