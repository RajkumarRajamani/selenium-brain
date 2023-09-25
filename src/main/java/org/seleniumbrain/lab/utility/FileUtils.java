package org.seleniumbrain.lab.utility;

public class FileUtils {

    public static String getFilePathWithFileSeparator(String path) {
        return path.replaceAll("/", System.getProperty("file.separator"));
    }
}
