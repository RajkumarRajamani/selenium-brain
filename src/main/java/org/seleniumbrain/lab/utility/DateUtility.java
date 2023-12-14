package org.seleniumbrain.lab.utility;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {

    public static String parseDate(String inputDate, String requiredFormat) {

        Date outputDate = null;
        String[] possibleDateFormats =
              {
                    "yyyy.MM.dd G 'at' HH:mm:ss z",
                    "EEE, MMM d, ''yy",
                    "h:mm a",
                    "hh 'o''clock' a, zzzz",
                    "K:mm a, z",
                    "yyyyy.MMMMM.dd GGG hh:mm aaa",
                    "EEE, d MMM yyyy HH:mm:ss Z",
                    "yyMMddHHmmssZ",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                    "YYYY-'W'ww-u",
                    "EEE, dd MMM yyyy HH:mm:ss z", 
                    "EEE, dd MMM yyyy HH:mm zzzz",
                    "yyyy-MM-dd'T'HH:mm:ssZ",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz", 
                    "yyyy-MM-dd'T'HH:mm:sszzzz",
                    "yyyy-MM-dd'T'HH:mm:ss z",
                    "yyyy-MM-dd'T'HH:mm:ssz", 
                    "yyyy-MM-dd'T'HH:mm:ss",
                    "yyyy-MM-dd'T'HHmmss.SSSz",
                    "yyyy-MM-dd",
                    "yyyyMMdd",
                    "yyyy/MM/dd",
                    "dd/MM/yy",
                    "ddMMyyyy",
                    "dd-MM-yyyy",
                    "dd/MM/yyyy"
              };

        try {

            outputDate = DateUtils.parseDate(inputDate, possibleDateFormats);
            String parsedDate = formatDate(outputDate, requiredFormat);
            System.out.println("inputDate ==> " + inputDate + ", outputDate ==> " + parsedDate);
            return parsedDate;

        } catch (ParseException e) {
            System.out.println(inputDate);
            return inputDate;
        }


    }

    private static String formatDate(Date date, String requiredDateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(requiredDateFormat);
        String outputDateFormatted = df.format(date);
        return outputDateFormatted;
    }

    public static void main(String[] args) {
        String requiredFormat = "yyyy-MM-dd";

        DateUtility.parseDate("20181118", requiredFormat);
        DateUtility.parseDate("2018-11-18", requiredFormat);
        DateUtility.parseDate("18/11/18", requiredFormat);
        DateUtility.parseDate("18/11/2018", requiredFormat);
        DateUtility.parseDate("2018.11.18 AD at 12:08:56 PDT", requiredFormat);
        System.out.println("");
        DateUtility.parseDate("Wed, Nov 18, '18", requiredFormat);
        DateUtility.parseDate("12:08 PM", requiredFormat);
        DateUtility.parseDate("12 o'clock PM, Pacific Daylight Time", requiredFormat);
        DateUtility.parseDate("0:08 PM, PDT", requiredFormat);
        DateUtility.parseDate("02018.Nov.18 AD 12:08 PM", requiredFormat);
        System.out.println("");
        DateUtility.parseDate("Wed, 18 Nov 2018 12:08:56 -0700", requiredFormat);
        DateUtility.parseDate("181118120856-0700", requiredFormat);
        DateUtility.parseDate("2018-11-18T12:08:56.235-0700", requiredFormat);
        DateUtility.parseDate("2018-11-18T12:08:56.235-07:00", requiredFormat);
        DateUtility.parseDate("2018-W27-3", requiredFormat);
        DateUtility.parseDate("2018-sdsd", requiredFormat);
    }

}