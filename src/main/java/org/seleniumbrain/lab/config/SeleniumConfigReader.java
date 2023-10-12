package org.seleniumbrain.lab.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.exception.ConfigurationReadException;
import org.seleniumbrain.lab.utility.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Getter
@Slf4j
public class SeleniumConfigReader {

    private static final String SELENIUM_CONFIG = String.join(File.separator, "", "configs", "selenium-config.yml");
    private static final SeleniumConfigurations seleniumConfig;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            seleniumConfig = mapper.readValue(SeleniumConfigReader.class.getResourceAsStream(FileUtils.getFilePathWithFileSeparator(SELENIUM_CONFIG)), SeleniumConfigurations.class);
        } catch (IOException e) {
            throw new ConfigurationReadException("Exception while reading Framework Configuration");
        }
    }

    public static SeleniumConfigurations getConfigs() {
        return seleniumConfig;
    }

    public static final long defaultPageLoadTimeout = 20;
    public static final long defaultImplicitTimeout = 10;
    public static final long defaultFluentMaxTimeout = 10;
    public static final long defaultFluentPollingInterval = 250;
    public static final int defaultFailureRetryCount = 10;
    public static final String defaultTestLab = "local";
    public static final String defaultDownloadFolder = String.join(File.separator, System.getProperty("user.dir"), "output", "download", "");
    public static final String defaultOutputFolder = String.join(File.separator, System.getProperty("user.dir"), "output", "");

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

    public static String getDownloadFolder() {
        String downloadFolder = seleniumConfig.getTest().getOutput().getDownloadFolder();
        if(Objects.isNull(downloadFolder) || downloadFolder.isBlank()) return defaultDownloadFolder;
        else return downloadFolder;
    }

    public static String getDownloadFolder(String... dirs) {
        String downloadFolder = seleniumConfig.getTest().getOutput().getDownloadFolder();
        if(Objects.isNull(downloadFolder) || downloadFolder.isBlank()) return getFolderPath(defaultDownloadFolder, dirs);
        else return getFolderPath(downloadFolder, dirs);
    }

    public static String getOutputFolder() {
        String outputFolder = seleniumConfig.getTest().getOutput().getFolder();
        if(Objects.isNull(outputFolder) || outputFolder.isBlank()) return defaultOutputFolder;
        else return outputFolder;
    }

    public static String getOutputFolder(String... dirs) {
        String outputFolder = seleniumConfig.getTest().getOutput().getFolder();
        if(Objects.isNull(outputFolder) || outputFolder.isBlank()) return getFolderPath(defaultOutputFolder, dirs);
        else return getFolderPath(outputFolder, dirs);
    }

    private static String getFolderPath(String prefix, String... dirs) {
        prefix = Objects.nonNull(prefix) ? StringUtils.removeEnd(prefix, File.separator) : "";
        dirs = Objects.nonNull(dirs) && dirs.length > 0 ? dirs : new String[]{""};
        return String.join(File.separator, prefix, String.join(File.separator, dirs));
    }

}
