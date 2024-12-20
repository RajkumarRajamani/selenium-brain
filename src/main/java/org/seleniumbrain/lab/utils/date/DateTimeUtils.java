package org.seleniumbrain.lab.utils.date;

import org.seleniumbrain.lab.utility.date.DateFormats;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class DateTimeUtils {

    private static Locale locale;
    private static ZoneId zoneId;
    private static ZoneOffset zoneOffset;

    static {
        initializeWithStandards(Locale.getDefault(), ZoneId.systemDefault(), ZoneOffset.UTC);
    }

    public static void initializeWithStandards(Locale localeValue, ZoneId zoneIdValue, ZoneOffset zoneOffsetValue) {
        locale = Objects.nonNull(localeValue) ? localeValue : Locale.getDefault();
        zoneId = Objects.nonNull(zoneIdValue) ? zoneIdValue : ZoneId.systemDefault();
        zoneOffset = Objects.nonNull(zoneOffsetValue) ? zoneOffsetValue : ZoneOffset.UTC;
    }

    public static void setLocale(Locale localeValue) {
        locale = localeValue;
    }

    public static void setZoneId(ZoneId zoneIdValue) {
        zoneId = zoneIdValue;
    }

    public static void setZoneOffset(ZoneOffset zoneOffsetValue) {
        zoneOffset = zoneOffsetValue;
    }


    /*
    Given a date string,

    String isValid(String dateStr)
    String isValid(String dateStr, Locale locale)
    String getFormatOf(String dateStr)
    String getFormatOf(String dateStr, Locale locale)

    boolean isFormatAt(String dateStr, Format expectedFormat)

    String getFormatAt(String dateStr, DateFormat toFormat)
    String getFormatAt(String dateStr, )

    LocalDate toLocalDate(String dateStr)
    LocalDateTime toLocalDateTime(String dateStr)
    Date toDate(String dateStr)

    LocalDate getCurrentLocalDate()
    LocalDateTime getCurrentLocalDateTime()
    Date getCurrentDate()


     */

    /**
     * @param dateStr text representation of date input value
     * @param locale  region
     * @return {@code boolean} returns if given text input of date value is matching any of valid format from {@link DateFormats}
     */
    /**
     * Checks if the given date string is valid, according to any of the supported date formats.
     * This method attempts to parse the input string using each format defined in the {@link DateFormats} enum.
     *
     * @param dateStr The date string to validate. Can be null.
     * @param locale The locale to use for date parsing. This influences the interpretation of month names, day names, etc.
     * @return {@code true} if the date string is valid, according to at least one supported format; {@code false} otherwise, including if the input string is null.
     * @since 1.0
     */
    private static boolean isValid(String dateStr, Locale locale) {
        if (dateStr != null) {
            DateFormatEnum[] dateFormats = DateFormatEnum.values();
            boolean isValid = false;
            for (DateFormatEnum format : dateFormats) {
                try {
                    LocalDateTime date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(format.getFormat(), locale)).atStartOfDay();
                    isValid = true;
                } catch (Exception ignored) {
                }
            }
            return isValid;
        } else {
            return false;
        }
    }

}
