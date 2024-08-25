package org.seleniumbrain.lab.cucumber;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Random;

@Slf4j
public class NewTest {

    @SneakyThrows
    public static void main(String[] args) throws IOException, ParseException {


    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
