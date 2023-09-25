package org.seleniumbrain.lab.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seleniumbrain.lab.exception.ConfigurationReadException;
import org.seleniumbrain.lab.utility.FileUtils;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class SeleniumConfig {

    private static final Logger logger = LogManager.getLogger(SeleniumConfig.class);
    private static final Properties properties;
    private static final String PROPERTY_FILE = "/configs/selenium-config.properties";

    static {
        try {
            properties  = new Properties();
            properties.load(SeleniumConfig.class.getResourceAsStream(FileUtils.getFilePathWithFileSeparator(PROPERTY_FILE)));
        } catch (Exception e) {
            throw new ConfigurationReadException("Exception while reading Framework Configuration");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getTestEnvironment() {
        String env = properties.getProperty("test.environment");
        if(Objects.nonNull(env)) return env;
        else throw new ConfigurationReadException("'test.environment' property is not specified in selenium-config.properties file");
    }

    public static String getSeleniumPageLoadTimeout() {
        String pageLoadTimeout = properties.getProperty("selenium.wait.timeout.pageLoad.seconds");
        if(Objects.nonNull(pageLoadTimeout)) return pageLoadTimeout;
        else throw new ConfigurationReadException("'selenium.wait.timeout.pageLoad.seconds' property is not specified in selenium-config.properties file");
    }

    public static String getSeleniumImplicitTimeout() {
        String implicitTimeout = properties.getProperty("selenium.wait.timeout.implicit.seconds");
        if(Objects.nonNull(implicitTimeout)) return implicitTimeout;
        else throw new ConfigurationReadException("'selenium.wait.timeout.implicit.seconds' property is not specified in selenium-config.properties file");
    }

    public static String getFluentMaxTimeout() {
        String fluentMaxTimeout = properties.getProperty("selenium.wait.timeout.fluent.max.seconds");
        if(Objects.nonNull(fluentMaxTimeout)) return fluentMaxTimeout;
        else throw new ConfigurationReadException("'selenium.wait.timeout.fluent.max.seconds' property is not specified in selenium-config.properties file");
    }

    public static String getFluentPollingInterval() {
        String fluentPollingInterval = properties.getProperty("selenium.wait.timeout.fluent.polling.milliseconds");
        if(Objects.nonNull(fluentPollingInterval)) return fluentPollingInterval;
        else throw new ConfigurationReadException("'selenium.wait.timeout.fluent.polling.milliseconds' property is not specified in selenium-config.properties file");
    }

    public static String getFailureRetryCount() {
        String retryCount = properties.getProperty("selenium.failure.retryCount");
        if(Objects.nonNull(retryCount)) return retryCount;
        else throw new ConfigurationReadException("'selenium.failure.retryCount' property is not specified in selenium-config.properties file");
    }

    private static String executionOutputPath;
    private static String edgeDownloadPath;
    private static String chromeDownloadPath;
    private static String fireFoxDownloadPath;
    private static String safariDownloadPath;

    public static String getOutputFolder() {
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getTestEnvironment());
        System.out.println(getSeleniumPageLoadTimeout());
        System.out.println(getFluentMaxTimeout());
        System.out.println(getFailureRetryCount());
        System.out.println(getSeleniumImplicitTimeout());
        System.out.println(getFluentPollingInterval());

        System.out.println(System.getProperty("user.home"));
    }
}
