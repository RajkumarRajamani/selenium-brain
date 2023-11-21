package org.seleniumbrain.lab.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {

    public static String getFilePathWithFileSeparator(String path) {
        return path
                .replace("/", File.separator)
                .replace("\\", "/");
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
