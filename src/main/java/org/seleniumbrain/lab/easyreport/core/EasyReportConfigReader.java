package org.seleniumbrain.lab.easyreport.core;

import org.seleniumbrain.lab.config.SystemConfig;
import org.seleniumbrain.lab.easyreport.exception.EasyReportException;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

public class EasyReportConfigReader {

    private static Properties properties;
    private static final String defaultReportDirectory = String.join(File.separator, SystemConfig.getProjectDir(), "output", "easy-report", "");

    public EasyReportConfigReader() {
        this.loadProperties();
    }

    @SneakyThrows
    private void loadProperties() {
        if(Objects.isNull(properties)) {
            String fileNameToFind = "cucumber.properties";
            String rootDirectoryPath = String.join(File.separator, SystemConfig.getProjectDir(), "src", "");
            File rootDirectory = new File(rootDirectoryPath);
            File cucumberPropertyFile = null;

            try(Stream<Path> walkStream = Files.walk(rootDirectory.toPath())) {
                cucumberPropertyFile = walkStream.filter(p -> p.toFile().isFile())
                        .filter(f -> f.toString().endsWith(fileNameToFind))
                        .findFirst().orElseThrow(() -> new EasyReportException("'cucumber.properties' file is not found in project folder")).toFile();
            }

            properties = new Properties();
            properties.load(new FileInputStream(cucumberPropertyFile));
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getJsonReportPath() {
        String path = properties.getProperty("easyReport.format.json.conventional");
        return path == null ? defaultReportDirectory + "easy-cucumber-report.json" : path;
    }

    public String getCustomizedJsonReportPath() {
        String path = properties.getProperty("easyReport.format.json.customized");
        return path == null ? defaultReportDirectory + "easy-cucumber-html-data-set-report.json" : path;
    }

    public String getHtmlReportPath() {
        String path = properties.getProperty("easyReport.format.html.customized");
        return path == null ? defaultReportDirectory + "easy-cucumber-html-report.html" : path;
    }

    public String getEnvironment() {
        String environment = properties.getProperty("easyReport.project.info.environment");
        return environment == null ? "Default Test Environment" : environment;
    }

    public String getBrowser() {
        String browser = properties.getProperty("easyReport.project.info.browser");
        return browser == null ? "Default Browser" : browser;
    }

    public String getApplicationName() {
        String appName = properties.getProperty("easyReport.project.info.appName");
        return appName == null ? "Default Application Name" : appName;
    }

    public String getProjectDescription() {
        String projectDescription = properties.getProperty("easyReport.project.info.descriptionOrReleaseNotes");
        return projectDescription == null ? "Default Description" : projectDescription;
    }

    public String getOs() {
        return System.getProperty("os.name");
    }

    public String getProjectManger() {
        String projectManager = properties.getProperty("easyReport.project.info.project-manager");
        return projectManager == null ? "un-defined" : projectManager;
    }

    public String getDeliveryQualityManager() {
        String dqManager = properties.getProperty("easyReport.project.info.dq-manager");
        return dqManager == null ? "un-defined" : dqManager;
    }

    public String getDeliveryQualityLead() {
        String dqLead = properties.getProperty("easyReport.project.info.dq-lead");
        return dqLead == null ? "un-defined" : dqLead;
    }

    public String getProdReleaseName() {
        String releaseName = properties.getProperty("easyReport.project.info.release.name");
        return releaseName == null ? "un-defined" : releaseName;
    }

    public String getProdReleaseDate() {
        String releaseDate = properties.getProperty("easyReport.project.info.release.date");
        return releaseDate == null ? "un-defined" : releaseDate;
    }

    public String getSprintName() {
        String sprintName = properties.getProperty("easyReport.project.info.release.sprint");
        return sprintName == null ? "un-defined" : sprintName;
    }

    public static void main(String[] args) {
        EasyReportConfigReader config = new EasyReportConfigReader();
        System.out.println(config.getProperty("easyReport.format.html.customized"));
        System.out.println(config.getProperty("easyReport.format.html.customizeds"));
    }

}
