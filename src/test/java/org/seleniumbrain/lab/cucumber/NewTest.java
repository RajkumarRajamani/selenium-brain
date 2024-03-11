package org.seleniumbrain.lab.cucumber;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.seleniumbrain.lab.core.selenium.validator.Validator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class NewTest {

    @SneakyThrows
    public static void main(String[] args) throws IOException, ParseException {

        System.out.println(LocalDateTime.now().plusDays(0));
        System.out.println(CaseUtils.toCamelCase("true", true));

        System.out.println(new File("/Users/rajkumarrajamani/Documents/01-Project/intellij-idea/ws-a/selenium-brain/src/main/resources/monitor.csv").length());
        System.out.println(new File("/Users/rajkumarrajamani/Documents/01-Project/intellij-idea/ws-a/selenium-brain/src/main/resources/monitorss.csv").length());

        String text = "please unlock \"subtext\" after a month.";
        String subString = StringUtils.substring(text, StringUtils.indexOf(text, "\"") + 1, StringUtils.lastIndexOf(text, "\""));
        System.out.println("SubString = " + subString);

        System.out.println(Validator.getNumberWithThousandSeparator("500000.555", 0, Locale.UK));

//        String file = "/Users/rajkumarrajamani/Documents/01-Project/intellij-idea/ws-a/selenium-brain/src/main/resources/monitor.csv";
//        List<Prototype> prototype = (List<Prototype>) CSVUtility.readCsv(file, Prototype.class);
//        System.out.println(JsonBuilder.transformPojoToJsonNode(prototype).toPrettyString());

        Faker faker = new Faker();
        Double value = faker.number().randomDouble(2, 40000000, 5000000);
        System.out.println(MessageFormat.format("{0}", value).replaceAll(",", ""));

        System.out.println(faker.number().numberBetween(4, 9));

        System.out.println(getRandomNumberUsingNextInt(300000, 900000));
        System.out.println(new Random().nextDouble(400000000, 900000000));

        Double val = faker.number().randomDouble(2, new BigDecimal("90000000000.00").longValue(), new BigDecimal("100000000000.00").longValue());
//        val = faker.number().randomDouble(2, 1, 30);
        System.out.println(MessageFormat.format("{0}", val).replaceAll(",", ""));

    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
