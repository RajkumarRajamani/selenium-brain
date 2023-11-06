package org.seleniumbrain.lab.cucumber;

import com.github.javafaker.Faker;
import org.apache.commons.io.FilenameUtils;
import org.seleniumbrain.lab.utility.PathBuilder;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.nio.file.Paths;

public class Test1 {
    public static void main(String[] args) {
        File file = new File("src/test/resources/cucumber/input-files/test.txt");
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String contentType = fileTypeMap.getContentType(file);
        System.out.println(contentType);

        System.out.println(System.getProperty("user.home"));

        try {
            Paths.get("abc///?d/?e?.txt");
            System.out.println(true);
        } catch (Exception e) {
            System.out.println(false);
        }

        System.out.println(FilenameUtils.getPath("abc///?d/?e?.txt"));
        System.out.println(String.join(File.separator, System.getProperty("user.dir"), "output", "download", ""));
        System.out.println(String.join(File.separator, "", "output", "download", ""));

        System.out.println(PathBuilder.getDownloadFolder("a", "b", "c.txt"));
        System.out.println(PathBuilder.getOutputFolder("a", "b", "c.txt"));
        System.out.println(PathBuilder.getDownloadFolder("a", "b", "c.txt"));
        System.out.println(PathBuilder.getOutputFolder("a", "b", "c.txt"));
        System.out.println(PathBuilder.getDownloadFolder("a", "b", "c.txt"));
        System.out.println(PathBuilder.getOutputFolder("a", "b", "c.txt"));

        System.out.println(PathBuilder.getDownloadFolder());
        System.out.println(PathBuilder.getOutputFolder());

        Faker faker = new Faker();
        System.out.println(faker.address().latitude());
        System.out.println(faker.address().longitude());

        System.out.println("INR 4555 INR".replaceFirst("INR ", ""));

    }
}
