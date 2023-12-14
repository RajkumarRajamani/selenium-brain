package org.seleniumbrain.lab.cucumber;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.utility.FileUtils;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NewTest {

    private static String src = "src/test/resources/test/dir1/file1.txt";
    public static void main(String[] args) throws IOException {

        System.out.println(Files.exists(Paths.get(src)));
        System.out.println(FileUtils.getCount("src/test/resources/test/dir1"));
        System.out.println(PathBuilder.getDownloadArchiveFolder());

        System.out.println(FilenameUtils.concat(FilenameUtils.getPath(src), FilenameUtils.getBaseName(src)));
        String filePath = "a/b/cname";
        String name = FilenameUtils.removeExtension(FilenameUtils.getName(filePath));
        System.out.println(name
        );

//        Files.move(Paths.get(src), Paths.get("src/test/resources/test/dir2/file1.txt"));
        System.out.println(System.getProperty("user.home"));

        System.out.println(StringEscapeUtils.unescapeJava("this is unicode text \u00C1 \u00C9 \u00D3"));
        System.out.println(StringEscapeUtils.unescapeCsv("this is unicode text"));
        System.out.println(StringEscapeUtils.unescapeCsv("this is unicode text \u00C1 \u00C9 \u00D3"));

        String json = JsonBuilder.getObjectBuilder()
                .withEmptyNode()
                .append("name", "rajkumar")
                .append("age", "30")
                .append("dob", "1993").build()
                .toPrettyString();

        Details details = JsonBuilder.transformJsonToPojoObject(json, Details.class);
        System.out.println(details);


    }

    @Data
    public static class Details {
        private String name;
        private String age;
        private String dob;
    }
}
