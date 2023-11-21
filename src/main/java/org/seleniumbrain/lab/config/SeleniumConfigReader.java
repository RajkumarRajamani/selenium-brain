package org.seleniumbrain.lab.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.exception.ConfigurationReadException;
import org.seleniumbrain.lab.utility.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Getter
@Slf4j
public class SeleniumConfigReader {

    private static final String SELENIUM_CONFIG = String.join("/",  "", "configs", "selenium-config.yml");
    private static final SeleniumConfigurations seleniumConfig;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            seleniumConfig = mapper.readValue(
                    SeleniumConfigReader.class.getResourceAsStream(SELENIUM_CONFIG),
                    SeleniumConfigurations.class);
        } catch (IOException e) {
            throw new ConfigurationReadException("Exception while reading Framework Configuration.", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getPageLoadTimeoutInSeconds());
    }

    public static SeleniumConfigurations getConfigs() {
        return seleniumConfig;
    }

    public static final String defaultTestEnvironment = "qa";
    public static final long defaultPageLoadTimeout = 20;
    public static final long defaultImplicitTimeout = 10;
    public static final long defaultFluentMaxTimeout = 10;
    public static final long defaultFluentPollingInterval = 250;
    public static final int defaultFailureRetryCount = 10;
    public static final String defaultTestLab = "local";

    public static String getTestEnvironment() {
        String environment = seleniumConfig.getTest().getApp().getEnvironment();
        return Objects.nonNull(environment) && !environment.isBlank() ? environment : defaultTestEnvironment;
    }

    public static long getPageLoadTimeoutInSeconds() {
        long pageLoadTimeoutInSeconds = seleniumConfig.getWait().getTimeout().getPageLoad();
        if(pageLoadTimeoutInSeconds == 0L) return defaultPageLoadTimeout;
        else return pageLoadTimeoutInSeconds;
    }

    public static long getImplicitTimeoutInSeconds() {
        long implicitTimeoutInSeconds = seleniumConfig.getWait().getTimeout().getImplicit();
        if(implicitTimeoutInSeconds == 0L) return defaultImplicitTimeout;
        else return implicitTimeoutInSeconds;
    }

    public static long getFluentMaxTimeoutInSeconds() {
        long fluentMaxTimeoutInSeconds = seleniumConfig.getWait().getTimeout().getFluentMax();
        if(fluentMaxTimeoutInSeconds == 0L) return defaultFluentMaxTimeout;
        else return fluentMaxTimeoutInSeconds;
    }

    public static long getFluentPollingTimeoutInSeconds() {
        long fluentPollingTimeoutInSeconds = seleniumConfig.getWait().getTimeout().getFluentPolling();
        if(fluentPollingTimeoutInSeconds == 0L) return defaultFluentPollingInterval;
        else return fluentPollingTimeoutInSeconds;
    }

    public static int getFailureRetryCount() {
        int failureRetryCount = seleniumConfig.getWait().getTimeout().getRetryCount();
        if(failureRetryCount == 0) return defaultFailureRetryCount;
        else return failureRetryCount;
    }

    public static String getTestLabName() {
        String testLabName = seleniumConfig.getLabName();
        if(Objects.isNull(testLabName)) return defaultTestLab;
        else return testLabName;
    }
}
