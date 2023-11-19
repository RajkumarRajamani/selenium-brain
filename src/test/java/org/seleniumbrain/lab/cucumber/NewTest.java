package org.seleniumbrain.lab.cucumber;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NewTest {

    private static String src = "src/test/resources/test/dir1/file1.txt";
    public static void main(String[] args) throws IOException {
        String filePath = "a/b/cname";
        String name = FilenameUtils.removeExtension(FilenameUtils.getName(filePath));
        System.out.println(name
        );

        Files.move(Paths.get(src), Paths.get("src/test/resources/test/dir2/file1.txt"));

    }
}
