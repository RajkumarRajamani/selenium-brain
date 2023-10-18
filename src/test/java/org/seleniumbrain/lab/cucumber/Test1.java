package org.seleniumbrain.lab.cucumber;

import com.github.javafaker.Faker;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.selenium.driver.Browsers;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.selenium.validator.ElementValidator;
import org.seleniumbrain.lab.selenium.validator.TextBoxValidator;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.springframework.util.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.nio.file.InvalidPathException;
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

    }
}
