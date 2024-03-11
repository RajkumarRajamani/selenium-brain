package org.seleniumbrain.lab.utility.date;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TestClass {

    public static void main(String[] args) throws ParseException {
        List<String> dates = new ArrayList<String>() {
            {
                add("2024-03-06");
                add("2024-Mar-07");
                add("2024-March-08");
                add("2024-03-09 23:12:34");
                add("2024-03-10T23:12:34");
                add("2024-03-12T23:12:34.332");
                add("2024-03-11T23:12:34.332z");
                add("2024-03-12T23:12:34.332Z");
                add("2024-03-12T23:12:34.332+0530");
                add("2024-03-12 23:12:34.332+0530");
                add("2024/03/13");
                add("14-03-2024");
                add("15/03/2024");
                add("16032024");
                add("17-Mar-2024");
                add("18-March-2024");
                add("19 Mar, 2024");
                add("20 March, 2024");
                add("Mar 21, 2024");
                add("March 22, 2024");
                add("2014-12-03T10:05:59.5646+08:00");
                add("2014-12-03 10:05:59.5646+08:00");

                add("----");
                add("02-13-2024");
                add("13-13-2024");
            }
        };

        String printFormat = "%-30s %-12s %-50s %-50s";

        // test isValidDate
        for (String dateString : dates) {
            String s = String.format(printFormat, dateString, " is valid? ", DateTimeUtility.isValidDate(dateString), "");
            System.out.println(s);
        }

        System.out.println("");

        // test detectDateFormat
        for (String dateString : dates) {
            String s = String.format(printFormat, dateString, " has format ", DateTimeUtility.detectDateFormat(dateString), "");
            System.out.println(s);
        }

        // test containsZoneDetails
        for (String dateString : dates) {
            String s = String.format(printFormat, dateString, " contains zone details ? ", DateTimeUtility.containsZoneDetails(dateString, DateTimeUtility.detectDateFormat(dateString)), "");
            System.out.println(s);
        }

        // test convertTo
        for (String dateString : dates) {
            for (DateFormats format : DateFormats.values()) {
                String s = String.format(printFormat, dateString, " converted to ", format.getFormat(), DateTimeUtility.changeFormatTo(dateString, format));
                System.out.println(s);
            }
            System.out.println(StringUtils.repeat("=", 150));
        }

        // test convertTo
        for (String dateString : dates) {
            try {
                String s = String.format(printFormat, dateString, " in LocalDate is : ", DateTimeUtility.textToLocalDateTime(dateString), "");
                System.out.println(s);
            } catch (Exception ignored) {
                String s = String.format(printFormat, dateString, " in LocalDate is : ", "Exception Occured", "");
                System.out.println(s);
            }
        }

        System.out.println(StringUtils.repeat("=", 150));

        System.out.println(DateTimeUtility.getCurrentLocalDateTime(TimeZoneId.EUROPE_LONDON.id()));
        System.out.println(DateTimeUtility.getCurrentLocalDateTime());

        System.out.println(StringUtils.repeat("=", 150));

        System.out.println(DateTimeUtility.getCurrentLocalDate(TimeZoneId.EUROPE_LONDON.id()));
        System.out.println(DateTimeUtility.getCurrentLocalDate());

    }

}
