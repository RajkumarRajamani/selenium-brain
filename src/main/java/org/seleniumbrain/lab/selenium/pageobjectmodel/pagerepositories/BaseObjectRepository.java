package org.seleniumbrain.lab.selenium.pageobjectmodel.pagerepositories;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.Dropdown;
import org.seleniumbrain.lab.selenium.elements.TextBox;
import org.seleniumbrain.lab.selenium.validator.AutoPrefixedTextBoxValidator;
import org.seleniumbrain.lab.selenium.validator.TextBoxValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@ScenarioScope
public class BaseObjectRepository {

    @Autowired
    private WebDriverWaits waits;

    @Autowired
    private WebDriverUtils webDriverUtils;

    @Autowired
    private TextBox textBox;

    @Autowired
    private Dropdown dropdown;

    @Autowired
    private TextBoxValidator textBoxValidator;

    @Autowired
    private AutoPrefixedTextBoxValidator autoPrefixedTextBoxValidator;

    @Autowired
    private SwagLabLoginPageOR swagLabLoginPageOR;

    @Autowired
    private SwagLabHomePageOR swagLabHomePageOR;

    private Assertions assertions;

    public <T> T withAssertion(Assertions assertions, T repository) {
        this.assertions = assertions;
        return repository;
    }

}
