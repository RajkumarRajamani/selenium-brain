package org.seleniumbrain.lab.selenium.pageobjectmodel.pagerepositories;

import io.cucumber.spring.ScenarioScope;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.selenium.elements.AnchorDropdown;
import org.seleniumbrain.lab.selenium.elements.Dropdown;
import org.seleniumbrain.lab.selenium.elements.Scroll;
import org.seleniumbrain.lab.selenium.elements.TextBox;
import org.seleniumbrain.lab.selenium.validator.AutoPrefixedTextBoxValidator;
import org.seleniumbrain.lab.selenium.validator.TextBoxValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class BaseObjectRepository {

    @Autowired
    public WebDriverWaits waits;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public TextBox textBox;

    @Autowired
    public Dropdown dropdown;

    @Autowired
    public AnchorDropdown anchorDropdown;

    @Autowired
    public Scroll jsExecutor;

    @Autowired
    public TextBoxValidator textBoxValidator;

    @Autowired
    public AutoPrefixedTextBoxValidator autoPrefixedTextBoxValidator;

    @Autowired
    public SwagLabLoginPageOR swagLabLoginPageOR;

    @Autowired
    public SwagLabHomePageOR swagLabHomePageOR;

    public Assertions assertions;

    public <T> T withAssertion(Assertions assertions, T repository) {
        this.assertions = assertions;
        return repository;
    }

}
