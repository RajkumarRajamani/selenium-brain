package org.seleniumbrain.lab.utils.date;

import lombok.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTimeUtilsArchived {

    @Setter
    private static Locale locale;

    @Setter
    private static ZoneId zoneId;

    @Setter
    private static ZoneOffset zoneOffset;

    static {
        initializeWithStandards(Locale.getDefault(), ZoneId.systemDefault(), ZoneOffset.UTC);
    }

    public static void loadDefaults() {
        initializeWithStandards(Locale.getDefault(), ZoneId.systemDefault(), ZoneOffset.UTC);
    }

    public static void initializeWithStandards(Locale localeValue, ZoneId zoneIdValue, ZoneOffset zoneOffsetValue) {
        locale = Objects.nonNull(localeValue) ? localeValue : Locale.getDefault();
        zoneId = Objects.nonNull(zoneIdValue) ? zoneIdValue : ZoneId.systemDefault();
        zoneOffset = Objects.nonNull(zoneOffsetValue) ? zoneOffsetValue : ZoneOffset.UTC;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormatOptions {
        private Locale locale;
        private ZoneId zoneId;
        private ZoneOffset zoneOffset;
    }

    /**
     * Checks if the given date string is valid, according to any of the supported date/time formats.
     * This method attempts to parse the input string using each format defined in the {@link DateTimeFormat} enum,
     * considering various date, time, and date-time types.
     *
     * @param dateStr The date string to validate. Can be {@code null}.
     * @param locale  The locale to use for date/time parsing. This influences the interpretation of month names, day names, etc.
     * @return {@code true} if the date string is valid according to at least one supported format;
     * {@code false} otherwise, including if the input string is {@code null}.
     * @since 1.0
     */
    public static boolean isValid(String dateStr, Locale locale) {
        if (Objects.isNull(dateStr)) return false;

        for (DateTimeFormat format : DateTimeFormat.values()) {
            try {
                if (tryParseLocalDateTime(dateStr, format, locale).isPresent() ||
                        tryParseLocalDate(dateStr, format, locale).isPresent() ||
                        tryParseYearMonth(dateStr, format, locale).isPresent() ||
                        tryParseDayMonth(dateStr, format, locale).isPresent() ||
                        tryParseLocalTime(dateStr, format, locale).isPresent())
                    return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public static boolean isValid(String dateStr) {
        return isValid(dateStr, locale);
    }

    /**
     * Attempts to determine the format of a given date string by iterating through predefined date formats.
     * This method uses case-insensitive parsing to match the input string against the available formats.
     *
     * @param dateStr The date string to analyze. Can be null.
     * @return The format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy") if a matching format is found;
     * {@code null} if the input string is null or if no matching format is found.
     * @since 1.0
     */
    public static String getFormatOf(String dateStr, Locale locale) {
        if (Objects.isNull(dateStr)) return null;

        for (DateTimeFormat format : DateTimeFormat.values()) {
            Optional<String> result = tryParseLocalDate(dateStr, format, locale);
            if (result.isPresent()) return result.get();

            result = tryParseLocalDateTime(dateStr, format, locale);
            if (result.isPresent()) return result.get();

            result = tryParseYearMonth(dateStr, format, locale);
            if (result.isPresent()) return result.get();

            result = tryParseDayMonth(dateStr, format, locale);
            if (result.isPresent()) return result.get();

            result = tryParseLocalTime(dateStr, format, locale);
            if (result.isPresent()) return result.get();
        }
        return null;
    }

    public static String getFormatOf(String dateStr) {
        return getFormatOf(dateStr, locale);
    }

    private static DateTimeFormatter getFormatter(DateTimeFormat format, Locale locale) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // Enable case-insensitive parsing
                .appendPattern(format.getFormat())
                .toFormatter(locale);
    }

    private static DateTimeFormatter getFormatter(String format, Locale locale) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // Enable case-insensitive parsing
                .appendPattern(format)
                .toFormatter(locale);
    }

    private static Optional<String> tryParseLocalDate(String dateStr, DateTimeFormat format, Locale locale) {
        try {
            DateTimeFormatter formatter = getFormatter(format, locale);
            LocalDate.parse(dateStr, formatter);
            return Optional.of(format.getFormat());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<String> tryParseLocalDateTime(String dateStr, DateTimeFormat format, Locale locale) {
        try {
            DateTimeFormatter formatter = getFormatter(format, locale);
            LocalDateTime.parse(dateStr, formatter);
            return Optional.of(format.getFormat());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<String> tryParseYearMonth(String dateStr, DateTimeFormat format, Locale locale) {
        try {
            DateTimeFormatter formatter = getFormatter(format, locale);
            YearMonth.parse(dateStr, formatter);
            return Optional.of(format.getFormat());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<String> tryParseDayMonth(String dateStr, DateTimeFormat format, Locale locale) {
        try {
            DateTimeFormatter formatter = getFormatter(format, locale);
            DayOfWeek.from(formatter.parse(dateStr));
            return Optional.of(format.getFormat());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<String> tryParseLocalTime(String dateStr, DateTimeFormat format, Locale locale) {
        try {
            DateTimeFormatter formatter = getFormatter(format, locale);
            LocalTime.parse(dateStr, formatter);
            return Optional.of(format.getFormat());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Checks if the format of the given date string matches the expected format.
     * This method uses the default locale for format detection.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format string (e.g., "yyyy-MM-dd").
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     */
    public static boolean isFormatAt(String dateStr, String expectedFormat) {
        return Objects.equals(getFormatOf(dateStr), expectedFormat);
    }

    /**
     * Checks if the format of the given date string matches the expected format, using the specified locale.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format string (e.g., "yyyy-MM-dd").
     * @param locale         The locale to use for format detection.
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     */
    public static boolean isFormatAt(String dateStr, String expectedFormat, Locale locale) {
        return Objects.equals(getFormatOf(dateStr, locale), expectedFormat);
    }

    /**
     * Checks if the format of the given date string matches the expected format from the {@link DateTimeFormat}.
     * This method uses the default locale for format detection.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format from the {@link DateTimeFormat}.
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     */
    public static boolean isFormatAt(String dateStr, DateTimeFormat expectedFormat) {
        return Objects.equals(getFormatOf(dateStr), expectedFormat.getFormat());
    }

    /**
     * Checks if the format of the given date string matches the expected format from the {@link DateTimeFormat}, using the specified locale.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format from the {@link DateTimeFormat}.
     * @param locale         The locale to use for format detection.
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     */
    public static boolean isFormatAt(String dateStr, DateTimeFormat expectedFormat, Locale locale) {
        return Objects.equals(getFormatOf(dateStr, locale), expectedFormat.getFormat());
    }

    /**
     * Formats a date string from one format to another.
     * <p>
     * This method attempts to parse the input date string (`dateStr`) according to the
     * inferred format (`inputDateFormat`) and then formats it to the desired output format
     * specified by the `toFormat` enum.
     *
     * @param dateStr  The date string to be formatted.
     * @param toFormat The desired output format as a DateFormatEnum value.
     * @return The formatted date string or the original string if parsing fails.
     */
    public static String formatTo(String dateStr, DateTimeFormat toFormat) {
        if (dateStr == null || toFormat == null) return dateStr;

        String inputDateFormat = getFormatOf(dateStr, locale);
        if (inputDateFormat == null) return dateStr; // If an input format is unknown, return the original

        DateTimeFormatter inputTextFormatter = getFormatter(inputDateFormat, locale);
        DateTimeFormatter outputTextFormatter = getFormatter(toFormat.getFormat(), locale);
        Optional<ZonedDateTime> parsedDateTime = getZonedDateTime(dateStr, inputTextFormatter);

        return parsedDateTime.map(zdt ->
                        zdt.withZoneSameInstant(zoneId)
                                .withZoneSameInstant(zoneOffset)
                                .format(outputTextFormatter))
                .orElse(dateStr);
    }

    public static String formatTo(String dateStr, DateTimeFormat toFormat, FormatOptions options) {
        setDateCoordinates(options);
        return formatTo(dateStr, toFormat);
    }

    private static void setDateCoordinates(FormatOptions options) {
        if (Objects.isNull(options)) return;

        Optional.ofNullable(options.getLocale()).ifPresent(DateTimeUtilsArchived::setLocale);
        Optional.ofNullable(options.getZoneId()).ifPresent(DateTimeUtilsArchived::setZoneId);
        Optional.ofNullable(options.getZoneOffset()).ifPresent(DateTimeUtilsArchived::setZoneOffset);
    }

    /**
     * Attempts to parse a String representation of a date and time into a LocalDateTime object.
     * <p>
     * This method first tries to identify the format of the date string based on the provided locale.
     * Then, it creates a DateTimeFormatter object suitable for parsing the specific format.
     * Finally, it attempts to parse the date string and convert it to a ZonedDateTime object.
     * If parsing is successful, the method extracts the LocalDateTime part and returns it.
     * Otherwise, it returns null.
     *
     * @param dateStr The String representation of the date and time to be parsed.
     * @return A LocalDateTime object representing the parsed date and time,
     * or null if parsing fails or the input string is null.
     */
    public static LocalDateTime toLocalDateTime(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = getFormatOf(dateStr, locale);
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = getFormatter(inputDateFormat, locale);
        Optional<ZonedDateTime> parsedDateTime = getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(ZonedDateTime::toLocalDateTime).orElse(null);
    }

    /**
     * Attempts to parse a String representation of a date into a LocalDate object.
     * <p>
     * This method follows the same logic as `toLocalDateTime`. It identifies the format,
     * creates a suitable DateTimeFormatter, attempts to parse the date string into a ZonedDateTime,
     * and then extracts and returns the LocalDate part. If parsing fails or the input string is null,
     * it returns null.
     *
     * @param dateStr The String representation of the date to be parsed.
     * @return A LocalDate object representing the parsed date, or null if parsing fails
     * or the input string is null.
     */
    public static LocalDate toLocalDate(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = getFormatOf(dateStr, locale);
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = getFormatter(inputDateFormat, locale);
        Optional<ZonedDateTime> parsedDateTime = getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(ZonedDateTime::toLocalDate).orElse(null);
    }

    /**
     * Attempts to parse a String representation of a time into a LocalTime object.
     * <p>
     * Similar to `toLocalDateTime` and `toLocalDate`, this method identifies the format,
     * creates a DateTimeFormatter, attempts to parse the string into a ZonedDateTime,
     * and then extracts and returns the LocalTime part. If parsing fails or the input string is null,
     * it returns null.
     *
     * @param dateStr The String representation of the time to be parsed.
     * @return A LocalTime object representing the parsed time, or null if parsing fails
     * or the input string is null.
     */
    public static LocalTime toLocalTime(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = getFormatOf(dateStr, locale);
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = getFormatter(inputDateFormat, locale);
        Optional<ZonedDateTime> parsedDateTime = getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(ZonedDateTime::toLocalTime).orElse(null);
    }

    /**
     * Attempts to parse a String representation of a date and time into a java.util.Date object.
     * <p>
     * This method follows a similar approach as the previous methods but converts the parsed
     * ZonedDateTime to an Instant first. Finally, it creates a java.util.Date object
     * from the Instant using `Date.from()`. If parsing fails or the input string is null,
     * it returns null.
     *
     * @param dateStr The String representation of the date and time to be parsed.
     * @return A java.util.Date object representing the parsed date and time,
     * or null if parsing fails or the input string is null.
     * @throws IllegalArgumentException if the parsed ZonedDateTime has a time zone
     *                                  offset of more than 18 hours from UTC (limitations of java.util.Date).
     */
    public static Date toDate(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = getFormatOf(dateStr, locale);
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = getFormatter(inputDateFormat, locale);
        Optional<ZonedDateTime> parsedDateTime = getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(zonedDateTime -> Date.from(zonedDateTime.toInstant())).orElse(null);
    }

    private static Optional<ZonedDateTime> getZonedDateTime(String dateStr, DateTimeFormatter inputTextFormatter) {
        return tryParseZonedDateTime(dateStr, inputTextFormatter)
                .or(() -> tryParseLocalDateTime(dateStr, inputTextFormatter).map(ldt -> ldt.atZone(zoneId)))
                .or(() -> tryParseLocalDate(dateStr, inputTextFormatter).map(ldt -> ldt.atStartOfDay(zoneId)))
                .or(() -> tryParseLocalTime(dateStr, inputTextFormatter).map(lt -> lt.atDate(LocalDate.now()).atZone(zoneId)))
                .or(() -> tryParseYearMonth(dateStr, inputTextFormatter).map(ym -> ym.atDay(1).atStartOfDay(zoneId)))
                .or(() -> tryParseMonthDay(dateStr, inputTextFormatter).map(md -> md.atYear(LocalDate.now().getYear()).atStartOfDay(zoneId)))
                .or(() -> tryParseDayWeek(dateStr, inputTextFormatter));
    }

    private static Optional<ZonedDateTime> tryParseZonedDateTime(String dateStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(ZonedDateTime.parse(dateStr, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<LocalDateTime> tryParseLocalDateTime(String dateStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(LocalDateTime.parse(dateStr, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<LocalDate> tryParseLocalDate(String dateStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(LocalDate.parse(dateStr, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<LocalTime> tryParseLocalTime(String dateStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(LocalTime.parse(dateStr, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<YearMonth> tryParseYearMonth(String dateStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(YearMonth.parse(dateStr, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<MonthDay> tryParseMonthDay(String dateStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(MonthDay.parse(dateStr, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Attempts to parse a date string as a DayOfWeek object and returns the next occurrence
     * of that day of the week as a ZonedDateTime.
     * <p>
     * This method uses the provided `formatter` to parse the `dateStr` and attempts to create
     * a DayOfWeek object. If successful, it finds the next occurrence of that day of the week
     * relative to the current date and returns a ZonedDateTime representing the start of the day
     * in the system's default zone. It returns an Optional containing the ZonedDateTime on success
     * or an empty Optional on failure.
     *
     * @param dateStr   The date string to be parsed.
     * @param formatter The DateTimeFormatter to use for parsing.
     * @return An Optional containing the ZonedDateTime for the next occurrence of the parsed day
     * of the week or empty Optional on failure.
     */
    private static Optional<ZonedDateTime> tryParseDayWeek(String dateStr, DateTimeFormatter formatter) {
        try {
            DayOfWeek dayOfWeek = DayOfWeek.from(formatter.parse(dateStr));
            LocalDate today = LocalDate.now();
            LocalDate nextDayOfWeek = today.with(TemporalAdjusters.nextOrSame(dayOfWeek));
            return Optional.of(nextDayOfWeek.atStartOfDay(ZoneId.systemDefault()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean compareDate(String dateStr1, String dateStr2) {
        String date1 = formatTo(dateStr1, DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        String date2 = formatTo(dateStr2, DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        return date1.equals(date2);
    }

    public static boolean compareDateTime(String dateStr1, String dateStr2) {
        String dateTime1 = formatTo(dateStr1, DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME);
        String dateTime2 = formatTo(dateStr2, DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME);
        return dateTime1.equals(dateTime2);
    }

    public static void main(String[] args) {
        test_formatTo();
    }

    private static void test_isValid() {
        System.out.println(isValid("2024-12-20 14:30:00"));
        System.out.println(isValid("Dec 20, 2024 02:30:00 am"));
        System.out.println(isValid("Dec 20, 2024 02:30:00 PM"));
        System.out.println(isValid("dec 20, 2024 02:30:00 Pm"));
        System.out.println(isValid("Dec 20, 2024 14:30:00"));
        System.out.println(isValid("12/20/2024 02:30:00 PM"));
        System.out.println(isValid("20241220143000"));
        System.out.println(isValid("2024-12-20 14:30:00.1234+05:30"));
        System.out.println(isValid("Fri, 20 Dec 2024 14:30:00 +0530"));
        System.out.println(isValid("2024 December"));
        System.out.println(isValid("2024 Dec"));
        System.out.println(isValid("20.12.2024"));
        System.out.println(isValid("Fri"));
        System.out.println(isValid("Friday"));
        System.out.println(isValid("20 Dec 2024"));
        System.out.println(isValid("2 December 2024"));
        System.out.println(isValid("2024-10"));
        System.out.println(isValid("23:12:45"));
        System.out.println(isValid("11:12:45 PM"));
        System.out.println(isValid("23:12:45 PM"));
        System.out.println(isValid("14:30:00.123456789"));
        System.out.println(isValid(""));
        System.out.println(isValid(null));
    }

    private static void test_getFormatOf() {
        System.out.println(getFormatOf("2024-12-20 14:30:00"));
        System.out.println(getFormatOf("Dec 20, 2024 02:30:00 am"));
        System.out.println(getFormatOf("Dec 20, 2024 02:30:00 PM"));
        System.out.println(getFormatOf("dec 20, 2024 02:30:00 Pm"));
        System.out.println(getFormatOf("Dec 20, 2024 14:30:00"));
        System.out.println(getFormatOf("12/20/2024 02:30:00 PM"));
        System.out.println(getFormatOf("20241220143000"));
        System.out.println(getFormatOf("2024-12-20 14:30:00.1234+05:30"));
        System.out.println(getFormatOf("Fri, 20 Dec 2024 14:30:00 +0530"));
        System.out.println(getFormatOf("2024 December"));
        System.out.println(getFormatOf("2024 Dec"));
        System.out.println(getFormatOf("20.12.2024"));
        System.out.println(getFormatOf("Fri"));
        System.out.println(getFormatOf("Friday"));
        System.out.println(getFormatOf("20 Dec 2024"));
        System.out.println(getFormatOf("2 December 2024"));
        System.out.println(getFormatOf("2024-10"));
        System.out.println(getFormatOf("23:12:45"));
        System.out.println(getFormatOf("11:12:45 PM"));
        System.out.println(getFormatOf("23:12:45 PM"));
        System.out.println(getFormatOf("14:30:00.123456789"));
        System.out.println(getFormatOf(""));
        System.out.println(getFormatOf(null));
    }

    private static void test_toMethods() {
        System.out.println(toLocalDateTime("2024-12-20 14:30:00"));
        System.out.println(toLocalDate("Dec 20, 2024 02:30:00 am"));
        System.out.println(toDate("12/20/2024 02:30:00 PM"));
        System.out.println(toLocalTime("20241220143000"));

        System.out.println(toLocalDateTime("2024 Dec"));
        System.out.println(toLocalDate("20.12.2024"));
        System.out.println(toDate("Fri"));
        System.out.println(toLocalTime("Friday"));
        System.out.println(getFormatOf("20 Dec 2024"));
        System.out.println(getFormatOf(""));
        System.out.println(getFormatOf(null));
    }

    private static void test_formatTo() {
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_OFFSET_DATE_TIME));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_INSTANT));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_ORDINAL_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_WEEK_DATE));

        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_SHORT_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MEDIUM_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_LONG_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_FULL_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_SHORT_DATE_TIME));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_SHORT_DATE_TIME_AM_PM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MEDIUM_DATE_TIME));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MEDIUM_DATE_TIME_am_pm));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MONTH_YEAR));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MONTH_DAY));

        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_SHORT_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_MEDIUM_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_LONG_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_FULL_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_SHORT_DATE_TIME));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_SHORT_DATE_TIME_24));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_DAY_MONTH));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_YEAR_MONTH));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_YYYYMMDD));

        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_SHORT_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_MEDIUM_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_LONG_DATE));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_FULL_DATE));

        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_JAPANESE_YYYY_MM_DD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_JAPANESE_YYYYMMDD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_JAPANESE_YYMMDD));

        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HHMMSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hhMMSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HHMM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hhMM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_HH_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_hh_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_HH_MM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_hh_MM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM_SS_SSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM_SS_SSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM_SS_SSSSSSSSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM_SS_SSSSSSSSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_HH_MM_SS_WITH_MILLIS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_hh_MM_SS_WITH_MILLIS));

        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYYMMDD_HHMMSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYYMMDD_HHMM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_MM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMMM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMM_DD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMMM_DD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_D_MMM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_D_MMMM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_DOT_MM_DOT_DD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_DD_DOT_MM_DOT_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_MMM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_MMMM));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MMM_DD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MMMM_DD));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_HH_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD__HH_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS_X));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_HH_MM_SS_SSS_X));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_HH_MM_SS_SSSS_XXX));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMM_YYYY_HH_MM_SS));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMMM_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMM_COMMA_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MON_COMMA_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_MMM_DD_COMMA_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_MMMM_DD_COMMA_YYYY));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DAY_OF_WEEK_SHORT));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DAY_OF_WEEK_FULL));
        System.out.println(formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));
        System.out.println(formatTo("", DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));
        System.out.println(formatTo(null, DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));
    }

}

