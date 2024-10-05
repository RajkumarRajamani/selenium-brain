package org.seleniumbrain.lab.utility.faker;

import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CustomFaker {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();


    // Method to generate fake file names for specific extensions
    public static String fakeFileName(List<String> extensions) {
        String extension = extensions.get(random.nextInt(extensions.size()));  // Randomly selects an extension
        LinkedList<String> randomWords = new LinkedList<>(faker.lorem().words(3));
        randomWords.add(extension);
        String fileName = String.join("-", randomWords);  // Generates a random word as a file name
        return fileName + "." + extension;  // Combines file name with an extension
    }

    public static String fakeFileName(String... extensions) {
        return fakeFileName(Arrays.asList(extensions));
    }
}