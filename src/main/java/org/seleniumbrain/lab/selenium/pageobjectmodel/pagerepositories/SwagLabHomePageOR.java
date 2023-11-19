package org.seleniumbrain.lab.selenium.pageobjectmodel.pagerepositories;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.seleniumbrain.lab.selenium.pageobjectmodel.spring.PageObjects;

@EqualsAndHashCode(callSuper = true)
@Data
@PageObjects
public class SwagLabHomePageOR extends BaseObjectRepository {

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenu;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutMenu;

    @FindBy(xpath = "//span[text()='Terms of Service']")
    private WebElement termsOfService;

    @FindBy(xpath = "//h3[text()='The Sauce Test Toolchain']")
    private WebElement textElement;

    @FindBy(xpath = "//code[text()='EventTarget']")
    private WebElement eventTarget;

    @FindBy(xpath = "//summary[text()='Events']/following-sibling::ol")
    private WebElement eventList;

    public SwagLabHomePageOR openSideMenu() {
        this.getHamburgerMenu().click();
        return this;
    }

    public SwagLabHomePageOR navigateToAboutPage() {
        this.getAboutMenu().click();
        jsExecutor.scrollElementIntoTopView(termsOfService);
        jsExecutor.scrollElementBy(textElement, 100, 200);
        webDriverUtils.getDriver().get("https://developer.mozilla.org/en-US/docs/Web/API/Window/scrollByLines");
        waits.pause(2000);
        jsExecutor.scrollElementIntoTopView(eventList);
        waits.pause(3000);
        return this;
    }

    public SwagLabHomePageOR test() {
        WebDriver driver = webDriverUtils.getDriver();
        driver.get("https://www.selenium.dev/");
        WebElement searchButton = driver.findElement(By.xpath("//button[@class='DocSearch DocSearch-Button']"));
        searchButton.click();
        WebElement searchBox = driver.findElement(By.id("docsearch-input"));
        textBox.setText(searchBox, "random");
        textBoxValidator
                .isDisplayed()
                .isEnabled()
                .isEditable()
                .apply(searchBox, "Search Box Field");
        return this;
    }
}
