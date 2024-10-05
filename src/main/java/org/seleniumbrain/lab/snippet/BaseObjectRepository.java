package org.seleniumbrain.lab.snippet;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.Setter;
import org.seleniumbrain.lab.core.config.SeleniumConfigReader;
import org.seleniumbrain.lab.core.cucumber.spring.configure.ScenarioState;
import org.seleniumbrain.lab.core.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.core.selenium.elements.AnchorDropdown;
import org.seleniumbrain.lab.core.selenium.elements.Dropdown;
import org.seleniumbrain.lab.core.selenium.elements.Scroll;
import org.seleniumbrain.lab.core.selenium.elements.TextBox;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.SharedStateKey;
import org.seleniumbrain.lab.core.selenium.validator.AutoPrefixedTextBoxValidator;
import org.seleniumbrain.lab.core.selenium.validator.CommonElementValidator;
import org.seleniumbrain.lab.core.selenium.validator.TextBoxValidator;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.snippet.pagerepositories.demo.SwagLabHomePageOR;
import org.seleniumbrain.lab.snippet.pagerepositories.demo.SwagLabLoginPageOR;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@ScenarioScope
public class BaseObjectRepository {

    @Autowired
    public WebDriverWaits waits;

    @Autowired
    public WebDriverUtils webDriverUtils;

    @Autowired
    public ScenarioState scenarioState;

    @Autowired
    public TextBox textBox;

    @Autowired
    public Dropdown dropdown;

    @Autowired
    public AnchorDropdown anchorDropdown;

    @Autowired
    public Scroll jsExecutor;

    @Autowired
    public CommonElementValidator commonElementValidator;

    @Autowired
    public TextBoxValidator textBoxValidator;

    @Autowired
    public AutoPrefixedTextBoxValidator autoPrefixedTextBoxValidator;

    @Autowired
    public SwagLabLoginPageOR swagLabLoginPageOR;

    @Autowired
    public SwagLabHomePageOR swagLabHomePageOR;

    @Autowired
    public Assertions assertions;

    @Getter
    @Setter
    public String sharedValue;

    public <T> T withAssertion(Assertions assertions, T repository) {
        this.assertions = assertions;
        return repository;
    }

    private int seqNo = 0;
    public String getFileNameForScenario(String fileNameKey) {
        seqNo = seqNo + 1;
        String prefix = scenarioState.getCacheText().get(SharedStateKey.SCENARIO_NAME_PREFIX);
        String defaultPrefix = String.join(
                "_",
                SeleniumConfigReader.getTestEnvironment(),
                String.format("%03d", seqNo),
                fileNameKey
        );
        return Objects.nonNull(prefix) && !prefix.isBlank() ? String.join("_", PathBuilder.getOutputFolder(), prefix + fileNameKey) : defaultPrefix;
    }

}
