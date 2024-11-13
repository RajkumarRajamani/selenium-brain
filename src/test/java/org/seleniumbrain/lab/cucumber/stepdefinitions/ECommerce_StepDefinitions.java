package org.seleniumbrain.lab.cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import org.seleniumbrain.lab.core.config.AUTConfigReader;
import org.seleniumbrain.lab.snippet.BaseObjectRepository;

public class ECommerce_StepDefinitions extends BaseObjectRepository{

    @Given("Navigate to the homepage")
    public void navigateToHomePage() {
        try {

            webDriverUtils.launchUrl("https://www.amazon.in/");
            waits.untilPageLoadComplete(30);

            amazonHomePage
                    .searchWithKeyword("Macbook")
                    .setPriceRange(90000);
            webDriverUtils.attachScreenshot("Screenshot");
//            String url = AUTConfigReader.get().getUiConfig().getUrl();
//            System.out.println(url);
//            webDriverUtils.launchUrl(url);
//
//            String host = AUTConfigReader.get().getApiConfig().getHost().get("host1");
//            System.out.println(host);

        } finally {
            assertions.assertAll();
        }
    }


}
