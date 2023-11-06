package org.seleniumbrain.lab.easyreport.util.dateutils;

import lombok.SneakyThrows;
import org.apache.commons.validator.GenericValidator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateUtils {

    public static boolean isDateValue(String dateString) {
        if (Objects.nonNull(dateString)) {
//            for(DateFormats format : DateFormats.values()) {
//                if(GenericValidator.isDate(dateString, format.get(), false)) {
//                    return true;
//                }
//            }
            return GenericValidator.isDate(dateString, DateFormats.YYYYMMDD_HYPHEN.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.YYYYMMDD_SLASH.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.YYYYMMMDD_HYPHEN.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.YYYYDDMM_HYPHEN.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.YYYYDDMM_SLASH.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.MMDDYYYY_HYPHEN.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.MMDDYYYY_SLASH.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.DDMMYYYY_HYPHEN.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.DDMMYYYY_SLASH.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.DDMMYYYY.get(), false) ||
                    GenericValidator.isDate(dateString, DateFormats.DDMMMYYYY_HYPHEN.get(), false);
        }
        return false;
    }


    @SneakyThrows
    public static String getDateStringAtFormat(String dateString, DateFormats fromFormat, DateFormats toFormat) {
        String defaultFormat = DateFormats.YYYYMMDD_HYPHEN.get();

        if(isDateValue(dateString)) {
            DateFormat df = new SimpleDateFormat(fromFormat.get());
            Date date = df.parse(dateString);
            if(Objects.nonNull(toFormat))
                return new SimpleDateFormat(toFormat.get()).format(date);
            else
                return new SimpleDateFormat(defaultFormat).format(date);
        }

        return dateString;
    }

    public static void main(String[] args) {
        System.out.println(isDateValue("03333333-05-1992"));
    }
}
