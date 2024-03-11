package org.seleniumbrain.lab.utility.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class DateTimeUtility {

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


    /**
     * @param dateStr text representation of date input value
     * @param locale  region
     * @return {@code boolean} returns if given text input of date value is matching any of valid format from {@link DateFormats}
     */
    private static boolean isValidDate(String dateStr, Locale locale) {
        if (dateStr != null) {
            DateFormats[] dateFormats = DateFormats.values();
            boolean isValid = false;
            for (DateFormats format : dateFormats) {
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

    public static boolean isValidDate(String dateStr) {
        return isValidDate(dateStr, locale);
    }

    /**
     * @param dateString text representation of date input value
     * @param locale     region
     * @return the matching format from {@link DateFormats} enum list
     * @implNote It detects the format of given text date input based on given {@link Locale} from the list available at {@link DateFormats}
     */
    public static String detectDateFormat(String dateString, Locale locale) {
        DateFormats[] dateFormats = DateFormats.values();
        for (DateFormats format : dateFormats) {
            try {
                LocalDateTime date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format.getFormat(), locale)).atStartOfDay();
                return format.getFormat();
            } catch (Exception ignored) {
            }
        }
        return "unknown";
    }

    /**
     * @param dateString The input date string to be detected the format for.
     * @return The format of the given input date string if it matches any one of the formats available at {@link DateFormats}
     */
    public static String detectDateFormat(String dateString) {
        return detectDateFormat(dateString, locale);
    }

    public static boolean isFormatMatching(String dateString, DateFormats format) {
        return detectDateFormat(dateString).equalsIgnoreCase(format.getFormat());
    }

    /**
     * @param dateInputString The input date string to be converted.
     * @param outputFormat    The desired format to which the date string should be converted.
     * @return The converted date string in the specified output format.<br>
     * <p>If the given input date does not match any of the format from {@link DateFormats} then will return same input text as an output</p>
     * @implNote It converts the given date string to any given date format among values maintained at {@link DateFormats}
     */
    public static String changeFormatTo(String dateInputString, DateFormats outputFormat) {
        return changeFormatTo(dateInputString, outputFormat, zoneId, zoneOffset, locale);
    }

    public static String changeFormatTo(String dateInputString, DateFormats outputFormat, ZoneId zoneId, ZoneOffset zoneOffset, Locale locale) {
        try {
            if (isValidDate(dateInputString, locale)) {
                // if an output format needs time details
                if (outputFormat.getFormat().contains("HH")) {
                    String inputDateFormat = detectDateFormat(dateInputString, locale);
                    DateTimeFormatter inputTextFormatter = DateTimeFormatter.ofPattern(inputDateFormat, locale);
                    DateTimeFormatter outputTextFormatter = DateTimeFormatter.ofPattern(outputFormat.getFormat(), locale);
                    // if an input text contains both time details and zone details, follow the below steps to convert text to the required date format
                    if (inputDateFormat.contains("HH") && containsZoneDetails(dateInputString, inputDateFormat)) {
                        return ZonedDateTime
                                .parse(dateInputString, inputTextFormatter)
                                .format(outputTextFormatter);
                    }
                    // if an input text contains time details without zone details, follow the below steps to convert text to the required date format
                    else if (inputDateFormat.contains("HH") && !containsZoneDetails(dateInputString, inputDateFormat)) {
                        return LocalDateTime
                                .parse(dateInputString, inputTextFormatter)
                                .atZone(zoneId)
                                .format(outputTextFormatter);
                    }
                    // if an input text does not contain both time details completely, follow the below steps to convert text to the required date format
                    else {
                        return LocalDate
                                .parse(dateInputString, inputTextFormatter)
                                .atStartOfDay()
                                .atOffset(zoneOffset)
                                .format(outputTextFormatter);
                    }
                } else {
                    return LocalDate.parse(dateInputString, DateTimeFormatter.ofPattern(detectDateFormat(dateInputString, locale), locale)).format(DateTimeFormatter.ofPattern(outputFormat.getFormat(), locale));
                }
            } else return dateInputString;
        } catch (Exception e) {
            return "Unable to Convert " + dateInputString + " to required format " + outputFormat.getFormat();
        }
    }

    public static boolean containsZoneDetails(String dateText, String dateFormat) {
        try {
            // Attempt to parse the date text into a ZonedDateTime object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
            // If parsing succeeds, it contains zone details
            return true;
        } catch (Exception e) {
            // Parsing failed, it does not contain zone details
            return false;
        }
    }

    public static LocalDate textToLocalDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(detectDateFormat(dateStr, locale), locale));
        } catch (Exception e) {
            throw new IllegalArgumentException("Given date string is not matching with DateFormats enum");
        }
    }

    public static LocalDate getCurrentLocalDate(ZoneId zoneId) {
        return LocalDate.now(zoneId);
    }

    public static LocalDate getCurrentLocalDate() {
        return getCurrentLocalDate(zoneId);
    }


    public static LocalDateTime textToLocalDateTime(String dateStr) {
        try {
            String dateConvertedToAStandardFormatThatCanBeParsedUsingLocalDateTime = changeFormatTo(dateStr, DateFormats.YYYY_MM_DD_T_HH_MM_SS_SSS_X);
            return LocalDateTime.parse(dateConvertedToAStandardFormatThatCanBeParsedUsingLocalDateTime, DateTimeFormatter.ofPattern(DateFormats.YYYY_MM_DD_T_HH_MM_SS_SSS_X.getFormat(), locale));
        } catch (Exception e) {
            throw new IllegalArgumentException("Given date string is not matching with DateFormats enum");
        }
    }

    public static LocalDateTime getCurrentLocalDateTime(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return getCurrentLocalDateTime(zoneId);
    }
}
