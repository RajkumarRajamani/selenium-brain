package org.seleniumbrain.lab.utility;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@ScenarioScope
public class FileUtils {

    public static String getFilePathWithFileSeparator(String path) {
        return path
                .replace("/", File.separator)
                .replace("\\", "/");
    }

    public static long getCount(String path) throws IOException {
        try(Stream<Path> walk = Files.walk(Paths.get(path))) {
            return walk.filter(Files::isDirectory)
                    .count();
        }
    }

    public static List<Path> listFiles(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public static List<Path> listDirectories(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isDirectory)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public static List<Path> findByFileExtension(Path path, String fileExtension)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isRegularFile)   // is a file
                    .filter(p -> p.getFileName().toString().endsWith(fileExtension))
                    .collect(Collectors.toList());
        }
        return result;

    }

    public static String findAndGetPathIfPresentOrDefaultToInputName(List<String> locationsToSearch, String fileName)
            throws IOException {

        List<String> results = new ArrayList<>();
        for (String location : locationsToSearch) {
            Path pathToSearch = Paths.get(location);
            String path = StringUtils.EMPTY;
            try(Stream<Path> walk = Files.walk(pathToSearch)) {
                List<Path> matchingPaths = walk
                        .filter(Files::isReadable)      // read permission
                        .filter(Files::isRegularFile)   // is a file
                        .filter(p -> p.getFileName().toString().equalsIgnoreCase(fileName))
                        .toList();

                if(matchingPaths.isEmpty()) path = "";
                else if (matchingPaths.size() > 1) {
                    throw new RuntimeException(MessageFormat.format("Too many occurrences of file with name ({0}) at path ({2}) as below \n[\n{1}\n].",
                            fileName,
                            String.join("\n", matchingPaths.stream().map(Path::toString).toList()),
                            location
                            )
                    );
                } else {
                    path = matchingPaths.get(0).toAbsolutePath().toString();
                }

                if (!path.isBlank()) results.add(path);
                else log.info("No file found at " + location);
            }
        }

        if(results.isEmpty()) return fileName; // if not found, returning given input file name itself
        else if (results.size() > 1) throw new RuntimeException(MessageFormat.format("Too many occurrences of file with name ({0}) at given paths as below \n[\n{1}\n]",
                fileName,
                String.join("\n", results)));
        else return results.get(0);
    }

    public static List<Path> findByFileName(Path path, String fileName)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> result;
        // walk file tree, no more recursive loop
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isReadable)      // read permission
                    .filter(Files::isRegularFile)   // is a file
                    .filter(p -> p.getFileName().toString().equalsIgnoreCase(fileName))
                    .collect(Collectors.toList());
        }
        return result;

    }

    public static List<Path> findByFileSize(Path path, long fileSizeInBytes)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> result;
        // walk file tree, no more recursive loop
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isReadable)              // read permission
                    .filter(p -> !Files.isDirectory(p))     // is a file
                    .filter(p -> checkFileSize(p, fileSizeInBytes))
                    .collect(Collectors.toList());
        }
        return result;

    }

    private static boolean checkFileSize(Path filePath, long fileSize) {
        boolean result = false;
        try {
            if (Files.size(filePath) >= fileSize) {
                result = true;
            }
        } catch (IOException e) {
            System.err.println("Unable to get the file size of this file: " + filePath);
        }
        return result;
    }

    public void writeFileAsJson(String fileName, String jsonContent) {
        try {
            String completeFileName = String.join(".", String.join("_", fileName, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))), "json");
            org.apache.commons.io.FileUtils.writeStringToFile(new File(completeFileName), jsonContent, Charset.defaultCharset());
        } catch (Exception e) {
            log.error("Error while writing json into file '" + fileName + "'" + e.getMessage());
        }
    }
}
