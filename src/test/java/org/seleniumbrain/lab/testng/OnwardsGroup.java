package org.seleniumbrain.lab.testng;

import io.cucumber.java.hu.Ha;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class OnwardsGroup {

    public static void main(String[] args) {


        String s = "rajkumar";

        char[] arr = s.toCharArray();


        Map<Character, Integer> map = new HashMap<>();
        for (char ch : arr) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }

        map.forEach((key, value) -> System.out.println(key + " repeated " + value));

    }

}
