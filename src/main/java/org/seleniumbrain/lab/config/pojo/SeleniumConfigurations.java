package org.seleniumbrain.lab.config.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SeleniumConfigurations {

    private String labName;
    private Test test;
    private AutomationLab lab;
    private SeleniumWaitDuration wait;

    @Data
    public static class Test {
        private Application app;
        private Output output;
    }

    @Data
    public static class Application {
        private String environment;
    }

    @Data
    public static class Output {
        private String outputFolder;
        private String downloadFolder;
        private String apiLogPath;
    }

    @Data
    public static class AutomationLab {
        private LocalLab localLab;
        private GridLab gridLab;
    }

    @Data
    public static class LocalLab {
        private String browserName;
        private BrowserOptionsDetails chrome;
        private BrowserOptionsDetails edge;
        private BrowserOptionsDetails firefox;
    }

    @Data
    public static class BrowserOptionsDetails {
        private String browserLogFilePath;

        private String browserVersion;
        private boolean acceptInsecureCerts;
        private String unhandledPromptBehavior;
        private String binary;
        private List<String> arguments;
        private List<String> extensions;
        private List<String> excludeSwitches;
        private List<Preference> prefs;
    }

    @Data
    public static class Preference {
        private String key;
        private Object value;
    }

    @Data
    public static class GridLab {
        private Hub hub;
        private List<Node> nodes;
    }

    @Data
    public static class Hub {
        private String name;
        private String url;
    }

    @Data
    public static class Node {
        private String name;
        private String url;
        private int maxBrowserInstance;
        private int maxBrowserSessions;
        private String browserName;
        private String browserVersion;
        private String platformName;
    }

    @Data
    public static class SeleniumWaitDuration {
        private Timeout timeout;
    }

    @Data
    public static class Timeout {
        private long pageLoad;
        private long implicit;
        private long fluentMax;
        private long fluentPolling;
        private int retryCount;
    }

}
