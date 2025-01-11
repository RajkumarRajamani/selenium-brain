package org.seleniumbrain.lab.cucumber.stepdefinitions;

import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.model.DesktopBrowserInfo;
import io.cucumber.java.en.Given;
import org.seleniumbrain.lab.core.config.AUTConfigReader;
import org.seleniumbrain.lab.snippet.BaseObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ECommerce_StepDefinitions extends BaseObjectRepository{

    @Autowired
    private TestBeanClass testBeanClass;

    @Given("Navigate to the homepage")
    public void navigateToHomePage() {
        try {

            Configuration config = new Configuration();
            config.setAppName("")
                            .addBrowser(new DesktopBrowserInfo(9,9).getRenderBrowserInfo());
            Eyes eye = new Eyes();

            testBeanClass.testMethod();
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
