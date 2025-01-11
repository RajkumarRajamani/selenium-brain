package org.seleniumbrain.lab.testng;

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hexaware_InterviewSesions {

    public static void main(String[] args) throws InterruptedException {

        String s = "Subha Sree";

        StringBuilder sb = new StringBuilder();

        char[] ar = s.toCharArray();
        IntStream.range(0, ar.length)
                .mapToObj(i -> ar[i])
                .map(String::valueOf)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .forEach((key, value) -> {
                    sb.append(key);
                    if(value > 1)
                        sb.append(value);
                });

        System.out.println(sb);

    }

}
