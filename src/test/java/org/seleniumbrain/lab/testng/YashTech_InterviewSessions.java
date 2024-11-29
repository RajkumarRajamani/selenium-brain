package org.seleniumbrain.lab.testng;

import io.cucumber.java.hu.Ha;
import io.restassured.RestAssured;

import java.util.*;

public class YashTech_InterviewSessions {


    public static void main(String[] args) {

        String s = "apple,apple,apple,orange,orange,mango";

        String[] arr = s.split(",");

        Map<String, Integer> map = new HashMap<>();

        for (String val : arr) {
            if (!map.containsKey(val))
                map.put(val, 1);
            else
                map.put(val, map.getOrDefault(val, 0) + 1);
        }

        map.forEach((key, value) -> System.out.println(key + " repeated " + value));

        Map.Entry<String, Integer> max = map.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue, Comparator.naturalOrder()))
                .get();

        System.out.println(max.getKey() + " : " + max.getValue());

        // Selenium
        // broken link
        // testng annotation
        // extent report with testng
        // hamcrest method

        // framework and role

        // SQL join
        // 3 rounds


        String ss = null;
        System.out.println(Objects.requireNonNullElse(ss, "Null Vlaue"));



    }







}
