package org.seleniumbrain.lab.testng;

import java.util.*;

public class LTIMindtree_InterviewSessions {

    public static void main(String[] args) {

        String name = "Rajkumar";

        Set<Character> vov = new HashSet<>() {
            {
                add('a');
                add('e');
                add('i');
                add('o');
                add('u');
            }
        };

        int count = 0;
        for(char c : name.toCharArray()) {
            if(vov.contains(c))
                count++;
        }

        System.out.println(count);


    }

}
