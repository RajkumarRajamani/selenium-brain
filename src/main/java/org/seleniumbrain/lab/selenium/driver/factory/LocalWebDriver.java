package org.seleniumbrain.lab.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.*;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.exception.ApiException;
import org.seleniumbrain.lab.exception.SeleniumBrainException;
import org.seleniumbrain.lab.selenium.driver.Browsers;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@ScenarioScope
public class LocalWebDriver implements DriverEngine {

    private WebDriver driver;
    private static final SeleniumConfigurations.LocalLab localLabConfig = SeleniumConfigReader.getConfigs().getLab().getLocalLab();

    /**
     * Initiates a new browser session of a given browser to perform the test case execution
     */
    @Override
    public void createSession() {

        try {
            String browserName = localLabConfig.getBrowserName();
            String browserLogFilePath = this.getLocalBrowserConfig().getBrowserLogFilePath();

            if (Objects.isNull(driver)) {
                SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails = Objects.requireNonNull(this.getLocalBrowserConfig(), "Local Browser config can not be null");
                Files.createDirectories(Paths.get(Objects.requireNonNull(FilenameUtils.getPath(browserLogFilePath))));

                switch (Browsers.valueOf(browserName.toUpperCase())) {
                    case CHROME -> {
                        ChromeOptions chromeOptions = this.addChromeOptions(browserOptionDetails, new ChromeOptions());
                        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                                .withLogFile(new File(browserLogFilePath))
                                .withReadableTimestamp(true)
                                .withAppendLog(true)
                                .withLogLevel(ChromiumDriverLogLevel.DEBUG)
                                .withVerbose(true)
                                .build();

                        log.info(MessageFormat.format("initiating chrome browser session for version ({0}) in local machine", chromeOptions.getBrowserVersion()));
                        driver = new ChromeDriver(chromeDriverService, chromeOptions);
                    }
                    case EDGE -> {
                        EdgeOptions edgeOptions = this.addEdgeOptions(browserOptionDetails, new EdgeOptions());
                        EdgeDriverService edgeDriverService = new EdgeDriverService.Builder()
                                .withLogFile(new File(browserLogFilePath))
                                .withReadableTimestamp(true)
                                .withAppendLog(true)
                                .withLoglevel(ChromiumDriverLogLevel.ALL)
                                .withVerbose(true)
                                .build();

                        log.info(MessageFormat.format("initiating edge browser session for version ({0}) in local machine", edgeOptions.getBrowserVersion()));
                        driver = new EdgeDriver(edgeDriverService, edgeOptions);
                    }
                    case FIREFOX -> {
                        FirefoxOptions firefoxOptions = this.addFirefoxOptions(browserOptionDetails, new FirefoxOptions());
                        FirefoxDriverService edgeDriverService = new GeckoDriverService.Builder()
                                .withLogFile(new File(browserLogFilePath))
                                .withLogLevel(FirefoxDriverLogLevel.DEBUG)
                                .withTruncatedLogs(false)
                                .build();

                        log.info(MessageFormat.format("initiating firefox browser session for version ({0}) in local machine", firefoxOptions.getBrowserVersion()));
                        driver = new FirefoxDriver(edgeDriverService, firefoxOptions);
                    }
                }

                driver.manage().deleteAllCookies();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(SeleniumConfigReader.getPageLoadTimeoutInSeconds()));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(SeleniumConfigReader.getImplicitTimeoutInSeconds()));
                driver.manage().window().maximize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return WebDriver instance for Local Web Automation
     */
    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

    private ChromeOptions addChromeOptions(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails, ChromeOptions options) {

        // set capabilities
        Map<String, Object> capabilitySet = this.getCapabilities(browserOptionDetails);
        capabilitySet.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> options.setCapability(entry.getKey(), entry.getValue()));

        // set binary
        String binaryPath = this.getBinary(browserOptionDetails);
        if (Objects.nonNull(binaryPath))
            options.setBinary(new File(binaryPath));

        // set arguments
        List<String> args = this.getArguments(browserOptionDetails);
        args.stream().filter(Objects::nonNull).forEach(options::addArguments);

        // set extensions
        List<String> extensions = this.getExtensions(browserOptionDetails);
        extensions.stream().filter(Objects::nonNull).forEach(ext -> options.addExtensions(new File(ext)));

        // set prefs
        List<SeleniumConfigurations.Preference> prefs = this.getPreferences(browserOptionDetails);
        if (!prefs.isEmpty()) {
            Map<String, Object> prefSet = prefs.stream()
                    .filter(preference -> Objects.nonNull(preference.getKey()) && Objects.nonNull(preference.getValue()))
                    .collect(Collectors.toMap(SeleniumConfigurations.Preference::getKey, SeleniumConfigurations.Preference::getValue));
            if (!prefSet.isEmpty()) options.setExperimentalOption("prefs", prefSet);
        }

        // set exclude switches
        List<String> switchesToExclude = this.getSwitchesToExclude(browserOptionDetails);
        if (!switchesToExclude.isEmpty()) {
            options.setExperimentalOption("excludeSwitches", switchesToExclude);
        }

        return options;
    }

    private EdgeOptions addEdgeOptions(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails, EdgeOptions options) {
        // set capabilities
        Map<String, Object> capabilitySet = this.getCapabilities(browserOptionDetails);
        capabilitySet.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> options.setCapability(entry.getKey(), entry.getValue()));

        // set binary
        String binaryPath = this.getBinary(browserOptionDetails);
        if (Objects.nonNull(binaryPath))
            options.setBinary(new File(binaryPath));

        // set arguments
        List<String> args = this.getArguments(browserOptionDetails);
        args.stream().filter(Objects::nonNull).forEach(options::addArguments);

        // set extensions
        List<String> extensions = this.getExtensions(browserOptionDetails);
        extensions.stream().filter(Objects::nonNull).forEach(ext -> options.addExtensions(new File(ext)));

        // set prefs
        List<SeleniumConfigurations.Preference> prefs = this.getPreferences(browserOptionDetails);
        if (!prefs.isEmpty()) {
            Map<String, Object> prefSet = prefs.stream()
                    .filter(preference -> Objects.nonNull(preference.getKey()) && Objects.nonNull(preference.getValue()))
                    .collect(Collectors.toMap(SeleniumConfigurations.Preference::getKey, SeleniumConfigurations.Preference::getValue));
            if (!prefSet.isEmpty()) options.setExperimentalOption("prefs", prefSet);
        }

        // set exclude switches
        List<String> switchesToExclude = this.getSwitchesToExclude(browserOptionDetails);
        if (!switchesToExclude.isEmpty()) {
            options.setExperimentalOption("excludeSwitches", switchesToExclude);
        }

        return options;
    }

    private FirefoxOptions addFirefoxOptions(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails, FirefoxOptions options) {
        // set capabilities
        Map<String, Object> capabilitySet = this.getCapabilities(browserOptionDetails);
        capabilitySet.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> options.setCapability(entry.getKey(), entry.getValue()));

        // set binary
        String binaryPath = this.getBinary(browserOptionDetails);
        if (Objects.nonNull(binaryPath))
            options.setBinary(Paths.get(binaryPath));

        // set arguments
        List<String> args = this.getArguments(browserOptionDetails);
        args.stream().filter(Objects::nonNull).forEach(options::addArguments);

        // set extensions
        List<String> extensions = this.getExtensions(browserOptionDetails);
        FirefoxProfile profile = new FirefoxProfile();
        if (!extensions.isEmpty()) {
            extensions.stream().filter(Objects::nonNull).forEach(ext -> profile.addExtension(new File(ext)));
            options.setProfile(profile);
        }

        // set prefs
        List<SeleniumConfigurations.Preference> prefs = this.getPreferences(browserOptionDetails);
        if (!prefs.isEmpty()) {
            prefs.stream()
                    .filter(preference -> Objects.nonNull(preference.getKey()) && Objects.nonNull(preference.getValue()))
                    .forEach(pref -> {
                        profile.setPreference(pref.getKey(), pref.getValue());
                        options.setProfile(profile);
                    });
        }

        // set exclude switches

        return options;
    }

    private Map<String, Object> getCapabilities(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails) {
        Map<String, Object> config = new HashMap<>();

        config.put("browserVersion", browserOptionDetails.getBrowserVersion());
        config.put("acceptInsecureCerts", browserOptionDetails.isAcceptInsecureCerts());
        config.put("unhandledPromptBehavior", browserOptionDetails.getUnhandledPromptBehavior());

        return config;
    }

    private List<String> getArguments(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails) {
        return Optional.ofNullable(browserOptionDetails.getArguments()).orElse(new ArrayList<>());
    }

    private String getBinary(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails) {
        return browserOptionDetails.getBinary();
    }

    private List<String> getExtensions(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails) {
        return Optional.ofNullable(browserOptionDetails.getExtensions()).orElse(new ArrayList<>());
    }

    private List<String> getSwitchesToExclude(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails) {
        return Optional.ofNullable(browserOptionDetails.getExcludeSwitches()).orElse(new ArrayList<>());
    }

    private List<SeleniumConfigurations.Preference> getPreferences(SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails) {
        return Optional.ofNullable(browserOptionDetails.getPrefs()).orElse(new ArrayList<>());
    }

    private SeleniumConfigurations.BrowserOptionsDetails getLocalBrowserConfig() {
        String browserName = Objects.requireNonNull(localLabConfig.getBrowserName(), "Browser Name must be specified for local run");
        SeleniumConfigurations.BrowserOptionsDetails browserOptionDetails = null;

        switch (Browsers.valueOf(browserName.toUpperCase())) {
            case CHROME -> browserOptionDetails = localLabConfig.getChrome();
            case EDGE -> browserOptionDetails = localLabConfig.getEdge();
            case FIREFOX -> browserOptionDetails = localLabConfig.getFirefox();
        }

        // sets the download path for browser
        // Assigning a path based on value provided in config yaml file.
        if (!browserOptionDetails.getPrefs().isEmpty()) {
            browserOptionDetails.getPrefs().stream()
                    .filter(pref -> Objects.nonNull(pref.getKey()) && pref.getKey().equalsIgnoreCase("download-path"))
//                    .filter(pref -> pref.getKey().equalsIgnoreCase("download-path"))
                    .findFirst()
                    .ifPresent(pref -> {
                        this.setBrowserDownloadPrefKey(pref, browserName);
                        pref.setValue(PathBuilder.getDownloadFolder());
//                        Optional.ofNullable(pref.getValue()).ifPresent(
//                                // if present
//                                prefValue -> {
//                                    this.setBrowserDownloadPrefKey(pref, browserName);
//                                    if (prefValue.toString().equalsIgnoreCase("default"))
//                                        pref.setValue(PathBuilder.getDefaultSystemDownloadFolder(browserName));
//
//                                    if (prefValue.toString().equalsIgnoreCase("dynamic"))
//                                        pref.setValue(PathBuilder.getDownloadFolder(browserName));
//                                }
//                        );
                    });
        }

        return browserOptionDetails;
    }

    private void setBrowserDownloadPrefKey(SeleniumConfigurations.Preference pref, String browserName) {
        switch (Browsers.valueOf(browserName.toUpperCase())) {
            case CHROME, EDGE -> pref.setKey("download.default_directory");
            case FIREFOX -> pref.setKey("browser.download.dir");
        }
    }

}
