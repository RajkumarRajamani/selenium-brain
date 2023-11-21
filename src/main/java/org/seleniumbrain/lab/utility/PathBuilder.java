package org.seleniumbrain.lab.utility;

import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PathBuilder {

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    private static String getDefaultDownloadFolder() {
        return String.join(File.separator, System.getProperty("user.dir"), "output", getRunCountText(), "download", "");
    }

    private static String getDefaultOutputFolder() {
        return String.join(File.separator, System.getProperty("user.dir"), "output", getRunCountText(), "");
    }

    private static String getDefaultApiLogOutputFolder() {
        return String.join(File.separator, System.getProperty("user.dir"), "output", "api-log", getRunCountText(), "");
    }

    public static String getDefaultSystemDownloadFolder() {
        return String.join(File.separator, System.getProperty("user.home"), "Downloads");
    }

    public static String getDefaultSystemDownloadFolder(String... nestedFolderNames) {
        return String.join(File.separator, System.getProperty("user.home"), "Downloads", String.join(File.separator, nestedFolderNames));
    }

    public static String getDownloadFolder() {
        String downloadFolder = seleniumConfig.getTest().getOutput().getDownloadFolder();
        if(Objects.isNull(downloadFolder) || downloadFolder.isBlank()) return getDefaultDownloadFolder();
        else return String.join(File.separator, downloadFolder, getRunCountText(), "");
    }

    public static String getDownloadFolder(String... dirs) {
        String downloadFolder = seleniumConfig.getTest().getOutput().getDownloadFolder();
        if(Objects.isNull(downloadFolder) || downloadFolder.isBlank()) return getFolderPath(getDefaultDownloadFolder(), dirs);
        else return getFolderPath(String.join(File.separator, downloadFolder, getRunCountText()), dirs);
    }

    public static String getOutputFolder() {
        String outputFolder = seleniumConfig.getTest().getOutput().getOutputFolder();
        if(Objects.isNull(outputFolder) || outputFolder.isBlank()) return getDefaultOutputFolder();
        else return String.join(File.separator, outputFolder, getRunCountText(), "");
    }

    public static String getOutputFolder(String... dirs) {
        String outputFolder = seleniumConfig.getTest().getOutput().getOutputFolder();
        if(Objects.isNull(outputFolder) || outputFolder.isBlank()) return getFolderPath(getDefaultOutputFolder(), dirs);
        else return getFolderPath(String.join(File.separator, outputFolder, getRunCountText()), dirs);
    }

    public static String getApiLogOutputFolder() {
        String apiLogOutputFolder = seleniumConfig.getTest().getOutput().getApiLogPath();
        if(Objects.isNull(apiLogOutputFolder) || apiLogOutputFolder.isBlank()) return getDefaultApiLogOutputFolder();
        else return String.join(File.separator, apiLogOutputFolder, getRunCountText(), "");
    }

    public static String getApiLogOutputFolder(String... dirs) {
        String apiLogOutputFolder = seleniumConfig.getTest().getOutput().getApiLogPath();
        if(Objects.isNull(apiLogOutputFolder) || apiLogOutputFolder.isBlank()) return getFolderPath(getDefaultApiLogOutputFolder(), dirs);
        else return getFolderPath(String.join(File.separator, apiLogOutputFolder, getRunCountText()), dirs);
    }

    private static String getFolderPath(String prefixPath, String... dirs) {
        prefixPath = Objects.nonNull(prefixPath) ? StringUtils.removeEnd(prefixPath, File.separator) : "";
        dirs = Objects.nonNull(dirs) && dirs.length > 0 ? dirs : new String[]{""};
        return String.join(File.separator, prefixPath, String.join(File.separator, dirs), "");
    }

    private static String runCountText = "";

    private static String getRunCountText() {

        if(Objects.isNull(runCountText) || runCountText.isBlank()) {
            String runCounterFile = "run-counter.json";
            String path = String.join(File.separator, System.getProperty("user.dir"), "src", "main", "resources", runCounterFile);
            File file = new File(path);
            JsonBuilder builder = JsonBuilder.getObjectBuilder();
            String timeStampSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            if(file.exists()) {
                builder.fromJsonFile(file).build();
                int runCount = builder.getNodeAt("runCount").asInt();
                runCountText = "Run-" + runCount + "_" + timeStampSuffix;
                builder.append("runCount", runCount + 1).build();
            } else {
                builder.withEmptyNode().build();
                runCountText = "Run-" + 1 + "_" + timeStampSuffix;
                builder.append("runCount", 2).build();
            }

            builder.writeTo(path);
            return runCountText;
        } else {
            return runCountText;
        }
    }

}
