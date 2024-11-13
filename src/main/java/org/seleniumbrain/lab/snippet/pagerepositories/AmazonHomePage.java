package org.seleniumbrain.lab.snippet.pagerepositories;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.PageObjects;
import org.seleniumbrain.lab.snippet.BaseObjectRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@PageObjects
public class AmazonHomePage extends BaseObjectRepository {

    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    private WebElement searchBar;

    @FindBy(xpath = "//input[@id='nav-search-submit-button']")
    private WebElement search;

    @FindBy(xpath = "//*[@id='p_36/range-slider_slider-item_upper-bound-slider']")
    private WebElement priceRangeHighBound;
    // prop:

    @FindBy(xpath = "//input[@aria-label='Go - Submit price range' and @type='submit']")
    private WebElement goButton;

    @FindBy(xpath = "//div[@data-cy='price-recipe']//span[@class='a-price']/span")
    private List<WebElement> priceList;

    public AmazonHomePage searchWithKeyword(String keyword) {
        searchBar.sendKeys(keyword);
        search.click();
        return this;
    }

    public AmazonHomePage setPriceRange(int expectedPriceRange) {

        String currentPriceRange = priceRangeHighBound.getAttribute("aria-valuetext").replaceAll("[^0-9]", "");
        int maxPrice = Integer.parseInt(currentPriceRange);

        int range = 189;
        JavascriptExecutor js = (JavascriptExecutor) webDriverUtils.getDriver();
        while (maxPrice >= expectedPriceRange) {
            js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));", priceRangeHighBound, range);
            currentPriceRange = priceRangeHighBound.getAttribute("aria-valuetext").replaceAll("[^0-9]", "");
            maxPrice = Integer.parseInt(currentPriceRange);
            range--;
        }

        goButton.click();

        waits.pause(4000);

        boolean isPriceItemWithinRange = priceList.stream()
                .filter(price -> !price.getText().isBlank())
                .allMatch(price -> {
                    String priceTxt = price.getText();
                    System.out.println("Price Text = " + priceTxt);
                    int itemPrice = Integer.parseInt(priceTxt.replaceAll("[^0-9.]", "").trim());
                    return itemPrice <= expectedPriceRange;
                });
        System.out.println("Size = " + priceList.size());

        assertions.isTrue("is everything within price range", isPriceItemWithinRange, "price range filter is failed");

        return this;
    }


}
