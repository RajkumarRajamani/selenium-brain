package org.seleniumbrain.lab.testng;

import org.apache.commons.lang3.math.NumberUtils;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;

import java.util.Objects;

public class JsonTest {

    public static void main(String[] args) {

        String val = "30,000,000,344,233.2332232323872897923";
        System.out.println("is creatable? " + NumberUtils.isCreatable(val));
        String s = NumberUtils.createDouble(val).toString();
        System.out.println(s);

        System.out.println(NumberUtils.createDouble(val).doubleValue());
        System.out.println(NumberUtils.createNumber(val).floatValue());
        System.out.println(NumberUtils.createNumber(val).toString());
//        System.out.println(NumberUtils.createDouble(val).doubleValue());
//        System.out.println(NumberUtils.createDouble(val).doubleValue());
//        System.out.println(NumberUtils.createDouble(val).doubleValue());
//        System.out.println(NumberUtils.createDouble(val).doubleValue());

    }

}
