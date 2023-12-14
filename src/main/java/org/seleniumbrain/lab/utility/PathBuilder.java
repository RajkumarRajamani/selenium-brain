package org.seleniumbrain.lab.utility;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.config.SystemConfig;
import org.seleniumbrain.lab.config.pojo.SeleniumConfigurations;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class PathBuilder {

    private static final SeleniumConfigurations seleniumConfig = SeleniumConfigReader.getConfigs();

    private static String getDefaultSystemDownloadFolder() {
        return String.join(File.separator, SystemConfig.getUserHomeDir(), "Downloads", "automation", getRunCountText(), "");
    }

    public static String getDefaultSystemDownloadFolder(String... nestedFolderNames) {
        return String.join(File.separator, SystemConfig.getUserHomeDir(), "Downloads", String.join(File.separator, nestedFolderNames));
    }

    private static String getDefaultOutputFolder() {
        return String.join(File.separator, SystemConfig.getProjectDir(), "test-output", getRunCountText(), "");
    }

    private static String getDefaultApiLogOutputFolder() {
        return String.join(File.separator, SystemConfig.getProjectDir(), "test-output", "api-log", getRunCountText(), "");
    }

    public static String createDirectoriesAndReturnPath(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (Exception e) {
            log.info(MessageFormat.format("Unable to create directories ({0}). \n Error is - {1}", path, e.getMessage()));
        }
        return path;
    }

    public static String getDownloadFolder() {
        String downloadFolder = seleniumConfig.getTest().getOutput().getDownloadFolder();
        if(Objects.isNull(downloadFolder) || downloadFolder.isBlank()) return createDirectoriesAndReturnPath(getDefaultSystemDownloadFolder());
        else return createDirectoriesAndReturnPath(String.join(File.separator, downloadFolder, getRunCountText(), ""));
    }

    public static String getDownloadFolder(String... dirs) {
        String downloadFolder = seleniumConfig.getTest().getOutput().getDownloadFolder();
        if(Objects.isNull(downloadFolder) || downloadFolder.isBlank()) return createDirectoriesAndReturnPath(getFolderPath(getDefaultSystemDownloadFolder(), dirs));
        else return createDirectoriesAndReturnPath(getFolderPath(String.join(File.separator, downloadFolder, getRunCountText()), dirs));
    }

    @SneakyThrows
    public static String getDownloadArchiveFolder() {
        String archivePath = createDirectoriesAndReturnPath(getFolderPath(getDownloadFolder(), "archived"));
        return createDirectoriesAndReturnPath(String.join(File.separator, archivePath, String.valueOf(FileUtils.getCount(archivePath))));
    }

    public static String getOutputFolder() {
        String outputFolder = seleniumConfig.getTest().getOutput().getOutputFolder();
        if(Objects.isNull(outputFolder) || outputFolder.isBlank()) return createDirectoriesAndReturnPath(getDefaultOutputFolder());
        else return createDirectoriesAndReturnPath(String.join(File.separator, outputFolder, getRunCountText(), ""));
    }

    public static String getOutputFolder(String... dirs) {
        String outputFolder = seleniumConfig.getTest().getOutput().getOutputFolder();
        if(Objects.isNull(outputFolder) || outputFolder.isBlank()) return createDirectoriesAndReturnPath(getFolderPath(getDefaultOutputFolder(), dirs));
        else return createDirectoriesAndReturnPath(getFolderPath(String.join(File.separator, outputFolder, getRunCountText()), dirs));
    }

    public static String getApiLogOutputFolder() {
        String apiLogOutputFolder = seleniumConfig.getTest().getOutput().getApiLogPath();
        if(Objects.isNull(apiLogOutputFolder) || apiLogOutputFolder.isBlank()) return createDirectoriesAndReturnPath(getDefaultApiLogOutputFolder());
        else return createDirectoriesAndReturnPath(String.join(File.separator, apiLogOutputFolder, getRunCountText(), ""));
    }

    public static String getApiLogOutputFolder(String... dirs) {
        String apiLogOutputFolder = seleniumConfig.getTest().getOutput().getApiLogPath();
        if(Objects.isNull(apiLogOutputFolder) || apiLogOutputFolder.isBlank()) return createDirectoriesAndReturnPath(getFolderPath(getDefaultApiLogOutputFolder(), dirs));
        else return createDirectoriesAndReturnPath(getFolderPath(String.join(File.separator, apiLogOutputFolder, getRunCountText()), dirs));
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
            String path = String.join(File.separator, SystemConfig.getProjectDir(), "src", "main", "resources", runCounterFile);
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
