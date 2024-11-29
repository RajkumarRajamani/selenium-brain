package org.seleniumbrain.lab.testng;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RGS_InterviewSessions {

    public static void main(String[] args) {

        String s="Programming";

        // way 1
        Set<Character> set = new LinkedHashSet<>();

        for(char c : s.toCharArray())  {
            set.add(c);
        }
        System.out.println(set);

        char[] arr = s.toCharArray();

        String res = IntStream.range(0, arr.length)
                .mapToObj(i -> arr[i])
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining());
        System.out.println(res);


    }

}
