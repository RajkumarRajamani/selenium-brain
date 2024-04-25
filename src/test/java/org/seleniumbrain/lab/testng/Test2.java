package org.seleniumbrain.lab.testng;

import org.apache.commons.lang3.StringUtils;

public class Test2 {

    public static void main(String[] args) {
        System.out.println(":" + StringUtils.substringBetween("1-45 of 1000", "-", " of") + ":");
    }

}
