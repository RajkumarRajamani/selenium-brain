package org.seleniumbrain.lab.testng;

import org.apache.commons.lang3.math.NumberUtils;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.utility.date.DateFormats;
import org.seleniumbrain.lab.utility.date.DateTimeUtility;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

import java.util.Objects;

public class JsonTest {

    public static void main(String[] args) {

        String dateVal = "2024-12-13T13:24:45";
        String val = DateTimeUtility.changeFormatTo(dateVal, DateFormats.DD_MMM_YYYY_HH_MM_SS);
        System.out.println(val);

    }

}
