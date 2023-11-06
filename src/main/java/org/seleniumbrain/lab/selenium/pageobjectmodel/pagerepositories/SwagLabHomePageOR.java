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

    public SwagLabHomePageOR openSideMenu() {
        this.getHamburgerMenu().click();
        return this;
    }

    public SwagLabHomePageOR navigateToAboutPage() {
        this.getAboutMenu().click();
        return this;
    }

    public SwagLabHomePageOR test() {
        WebDriver driver = getWebDriverUtils().getDriver();
        driver.get("https://www.selenium.dev/");
        WebElement searchButton = driver.findElement(By.xpath("//button[@class='DocSearch DocSearch-Button']"));
        searchButton.click();
        WebElement searchBox = driver.findElement(By.id("docsearch-input"));
        getTextBox().setText(searchBox, "random");
        getTextBoxValidator()
                .isDisplayed()
                .isEnabled()
                .isEditable()
                .apply(searchBox, "Search Box Field");
        return this;
    }
}
