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

/**
 * The `DateTimeUtils` class provides utility methods for date and time manipulation and formatting.
 * It supports various date and time formats and offers methods to validate, parse, and format date strings.
 *
 * <p>This class is designed to handle different locales and time zones, ensuring accurate date and time operations
 * across various regions and formats. It includes methods to:
 * <ul>
 *   <li>Validate date strings against supported formats</li>
 *   <li>Determine the format of a given date string</li>
 *   <li>Format date strings from one format to another</li>
 *   <li>Convert date strings to `LocalDateTime`, `LocalDate`, `LocalTime`, and `Date` objects</li>
 * </ul>
 *
 * <p>The class uses a `FormatOptions` inner class to encapsulate locale, time zone, and offset settings.
 * Default options can be set through constructors, and specific options can be passed to methods as needed.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * DateTimeUtils utils = new DateTimeUtils(Locale.US, ZoneId.of("America/New_York"));
 * String formattedDate = utils.formatTo("2024-12-20 14:30:00", DateTimeFormat.FORMAT_US_SHORT_DATE);
 * }
 * </pre>
 *
 * <p>Note: This class is immutable and thread-safe.
 *
 * @since 1.0
 */
public class DateTimeUtils {

    private FormatOptions defaultOptions;

    public DateTimeUtils() {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(Locale locale) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(ZoneId zoneId) {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(zoneId)
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils(Locale locale, ZoneId zoneId) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(zoneId)
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(Locale locale, ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils(ZoneId zoneId, ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(zoneId)
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils(Locale locale, ZoneId zoneId, ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(zoneId)
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils setDefaultOptions(FormatOptions options) {
        this.defaultOptions = options;
        return this;
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
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils(Locale.US);
     * boolean isValid = utils.isValid("2024-12-20 14:30:00", Locale.US);
     * System.out.println(isValid); // Output: true
     * }
     * </pre>
     */
    public boolean isValid(String dateStr, Locale locale) {
        if (dateStr == null) return false;

        for (DateTimeFormat format : DateTimeFormat.values()) {
            DateTimeFormatter formatter = getFormatter(format, locale);
            if (tryParse(dateStr, formatter, LocalDate.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalDateTime.class).isPresent() ||
                    tryParse(dateStr, formatter, YearMonth.class).isPresent() ||
                    tryParse(dateStr, formatter, MonthDay.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalTime.class).isPresent() ||
                    tryParseDayWeek(dateStr, formatter).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(String dateStr) {
        return this.isValid(dateStr, defaultOptions.getLocale());
    }

    /**
     * Attempts to determine the format of a given date string by iterating through predefined date formats.
     * This method uses case-insensitive parsing to match the input string against the available formats.
     *
     * @param dateStr The date string to analyze. Can be {@code null}.
     * @param locale  The locale to use for date/time parsing. This influences the interpretation of month names, day names, etc.
     * @return The format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy") if a matching format is found;
     * {@code null} if the input string is {@code null} or if no matching format is found.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils(Locale.US);
     * String format = utils.getFormatOf("2024-12-20 14:30:00", Locale.US);
     * System.out.println(format); // Output: yyyy-MM-dd HH:mm:ss
     * }
     * </pre>
     */
    public String getFormatOf(String dateStr, Locale locale) {
        if (dateStr == null) return null;

        for (DateTimeFormat format : DateTimeFormat.values()) {
            DateTimeFormatter formatter = getFormatter(format, locale);
            if (tryParse(dateStr, formatter, LocalDate.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalDateTime.class).isPresent() ||
                    tryParse(dateStr, formatter, YearMonth.class).isPresent() ||
                    tryParse(dateStr, formatter, MonthDay.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalTime.class).isPresent() ||
                    tryParseDayWeek(dateStr, formatter).isPresent()) {
                return format.getFormat();
            }
        }
        return null;
    }

    /**
     * Attempts to determine the format of a given date string by iterating through predefined date formats.
     * This method uses case-insensitive parsing to match the input string against the available formats.
     * Uses the default locale for date/time parsing.
     *
     * @param dateStr The date string to analyze. Can be {@code null}.
     * @return The format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy") if a matching format is found;
     * {@code null} if the input string is {@code null} or if no matching format is found.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * String format = utils.getFormatOf("2024-12-20 14:30:00");
     * System.out.println(format); // Output: yyyy-MM-dd HH:mm:ss
     * }
     * </pre>
     */
    public String getFormatOf(String dateStr) {
        return this.getFormatOf(dateStr, defaultOptions.getLocale());
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
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("2024-12-20", "yyyy-MM-dd");
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, String expectedFormat) {
        return Objects.equals(this.getFormatOf(dateStr), expectedFormat);
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
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("20.12.2024", "dd.MM.yyyy", Locale.GERMANY);
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, String expectedFormat, Locale locale) {
        return Objects.equals(this.getFormatOf(dateStr, locale), expectedFormat);
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
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, DateTimeFormat expectedFormat) {
        return Objects.equals(this.getFormatOf(dateStr), expectedFormat.getFormat());
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
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY);
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, DateTimeFormat expectedFormat, Locale locale) {
        return Objects.equals(this.getFormatOf(dateStr, locale), expectedFormat.getFormat());
    }

    /**
     * Formats a date string from one format to another.
     * <p>
     * This method attempts to parse the input date string (`dateStr`) according to the
     * inferred format (`inputDateFormat`) and then formats it to the desired output format
     * specified by the `toFormat` enum.
     *
     * @param dateStr  The date string to be formatted.
     * @param toFormat The desired output format as a {@link DateTimeFormat} value.
     * @return The formatted date string or the original string if parsing fails.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * String formattedDate = utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
     * System.out.println(formattedDate); // Output: 2024-12-20
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * String formattedDate = utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
     * System.out.println(formattedDate); // Output: invalid-date
     * }
     * </pre>
     */
    public String formatTo(String dateStr, DateTimeFormat toFormat) {
        if (dateStr == null || toFormat == null) return dateStr;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return dateStr;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        DateTimeFormatter outputTextFormatter = this.getFormatter(toFormat.getFormat(), defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);

        return parsedDateTime.map(zdt ->
                zdt.withZoneSameInstant(defaultOptions.zoneId)
                        .withZoneSameInstant(defaultOptions.zoneOffset)
                        .format(outputTextFormatter)
        ).orElse(dateStr);
    }

    /**
     * Formats a date string from one format to another using specific format options.
     * <p>
     * This method attempts to parse the input date string (`dateStr`) according to the
     * inferred format (`inputDateFormat`) and then formats it to the desired output format
     * specified by the `toFormat` enum, considering the provided `options`.
     *
     * @param dateStr  The date string to be formatted.
     * @param toFormat The desired output format as a {@link DateTimeFormat} value.
     * @param options  The format options to use for locale, time zone, and offset.
     * @return The formatted date string or the original string if parsing fails.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * String formattedDate = utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options);
     * System.out.println(formattedDate); // Output: 2024-12-20
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * String formattedDate = utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options);
     * System.out.println(formattedDate); // Output: invalid-date
     * }
     * </pre>
     */
    public String formatTo(String dateStr, DateTimeFormat toFormat, FormatOptions options) {
        setDefaultOptions(options);
        return formatTo(dateStr, toFormat);
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
    public LocalDateTime toLocalDateTime(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
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
    public LocalDate toLocalDate(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
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
    public LocalTime toLocalTime(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
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
    public Date toDate(String dateStr) {
        if (dateStr == null) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(zonedDateTime -> Date.from(zonedDateTime.toInstant())).orElse(null);
    }

    /**
     * Compares two date strings given in any different format to check if they represent the same date.
     * <p>
     * This method formats both date strings to the ISO local date format ("yyyy-MM-dd")
     * and then compares them for equality.
     *
     * @param dateStr1 The first date string to compare.
     * @param dateStr2 The second date string to compare.
     * @return {@code true} if both date strings represent the same date;
     * {@code false} otherwise, including if either date string is null or cannot be parsed.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDate = utils.compareDate("2024-12-20", "2024-12-20T14:30:00+05:30");
     * System.out.println(isSameDate); // Output: true
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDate = utils.compareDate("2024-12-20", "2024-12-21T14:30:00+05:30");
     * System.out.println(isSameDate); // Output: false
     * }
     * </pre>
     */
    public boolean compareDate(String dateStr1, String dateStr2) {
        String date1 = this.formatTo(dateStr1, DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        String date2 = this.formatTo(dateStr2, DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        return date1.equals(date2);
    }

    /**
     * Compares two date-time strings given in any different format to check if they represent the same date and time.
     * <p>
     * This method formats both date-time strings to the ISO local date-time format ("yyyy-MM-dd'T'HH:mm:ss")
     * and then compares them for equality.
     *
     * @param dateStr1 The first date-time string to compare.
     * @param dateStr2 The second date-time string to compare.
     * @return {@code true} if both date-time strings represent the same date and time;
     * {@code false} otherwise, including if either date-time string is null or cannot be parsed.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM");
     * System.out.println(isSameDateTime); // Output: true
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:31:00 PM");
     * System.out.println(isSameDateTime); // Output: false
     * }
     * </pre>
     */
    public boolean compareDateTime(String dateStr1, String dateStr2) {
        String dateTime1 = this.formatTo(dateStr1, DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME);
        String dateTime2 = this.formatTo(dateStr2, DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME);
        return dateTime1.equals(dateTime2);
    }

    /**
     * Compares two date-time strings given in any different format to check if they represent the same date and time, using specific format options.
     * <p>
     * This method formats both date-time strings to the ISO local date-time format ("yyyy-MM-dd'T'HH:mm:ss")
     * and then compares them for equality, considering the provided `options`.
     *
     * @param dateStr1 The first date-time string to compare.
     * @param dateStr2 The second date-time string to compare.
     * @param options  The format options to use for locale, time zone, and offset.
     * @return {@code true} if both date-time strings represent the same date and time;
     * {@code false} otherwise, including if either date-time string is null or cannot be parsed.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM", options);
     * System.out.println(isSameDateTime); // Output: true
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:31:00 PM", options);
     * System.out.println(isSameDateTime); // Output: false
     * }
     * </pre>
     */
    public boolean compareDateTime(String dateStr1, String dateStr2, FormatOptions options) {
        String dateTime1 = this.formatTo(dateStr1, DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX, options);
        String dateTime2 = this.formatTo(dateStr2, DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX, options);
        return dateTime1.equals(dateTime2);
    }

    private DateTimeFormatter getFormatter(String format, Locale locale) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // Enable case-insensitive parsing
                .appendPattern(format)
                .toFormatter(locale);
    }

    private DateTimeFormatter getFormatter(DateTimeFormat format, Locale locale) {
        return this.getFormatter(format.getFormat(), locale);
    }

    private Optional<ZonedDateTime> getZonedDateTime(String dateStr, DateTimeFormatter formatter) {
        return tryParse(dateStr, formatter, ZonedDateTime.class)
                .or(() -> tryParse(dateStr, formatter, LocalDateTime.class).map(ldt -> ldt.atZone(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, LocalDate.class).map(ld -> ld.atStartOfDay(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, LocalTime.class).map(lt -> lt.atDate(LocalDate.now()).atZone(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, YearMonth.class).map(ym -> ym.atDay(1).atStartOfDay(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, MonthDay.class).map(md -> md.atYear(LocalDate.now().getYear()).atStartOfDay(defaultOptions.getZoneId())))
                .or(() -> tryParseDayWeek(dateStr, formatter));
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
    private Optional<ZonedDateTime> tryParseDayWeek(String dateStr, DateTimeFormatter formatter) {
        try {
            DayOfWeek dayOfWeek = DayOfWeek.from(formatter.parse(dateStr));
            LocalDate today = LocalDate.now();
            LocalDate nextDayOfWeek = today.with(TemporalAdjusters.nextOrSame(dayOfWeek));
            return Optional.of(nextDayOfWeek.atStartOfDay(defaultOptions.getZoneId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private <T> Optional<T> tryParse(String dateStr, DateTimeFormatter formatter, Class<T> type) {
        try {
            if (type == ZonedDateTime.class) {
                return Optional.of(type.cast(ZonedDateTime.parse(dateStr, formatter)));
            } else if (type == LocalDateTime.class) {
                return Optional.of(type.cast(LocalDateTime.parse(dateStr, formatter)));
            } else if (type == LocalDate.class) {
                return Optional.of(type.cast(LocalDate.parse(dateStr, formatter)));
            } else if (type == LocalTime.class) {
                return Optional.of(type.cast(LocalTime.parse(dateStr, formatter)));
            } else if (type == YearMonth.class) {
                return Optional.of(type.cast(YearMonth.parse(dateStr, formatter)));
            } else if (type == MonthDay.class) {
                return Optional.of(type.cast(MonthDay.parse(dateStr, formatter)));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
//        DateTimeUtils util = new DateTimeUtils();
//        test_formatTo(util);

        DateTimeUtils utils = new DateTimeUtils();
        boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM");
        System.out.println(isSameDateTime);
    }

    private static void test_isValid(DateTimeUtils util) {
        System.out.println(util.isValid("2024-12-20 14:30:00"));
        System.out.println(util.isValid("Dec 20, 2024 02:30:00 am"));
        System.out.println(util.isValid("Dec 20, 2024 02:30:00 PM"));
        System.out.println(util.isValid("dec 20, 2024 02:30:00 Pm"));
        System.out.println(util.isValid("Dec 20, 2024 14:30:00"));
        System.out.println(util.isValid("12/20/2024 02:30:00 PM"));
        System.out.println(util.isValid("20241220143000"));
        System.out.println(util.isValid("2024-12-20 14:30:00.1234+05:30"));
        System.out.println(util.isValid("Fri, 20 Dec 2024 14:30:00 +0530"));
        System.out.println(util.isValid("2024 December"));
        System.out.println(util.isValid("2024 Dec"));
        System.out.println(util.isValid("20.12.2024"));
        System.out.println(util.isValid("Fri"));
        System.out.println(util.isValid("Friday"));
        System.out.println(util.isValid("20 Dec 2024"));
        System.out.println(util.isValid("2 December 2024"));
        System.out.println(util.isValid("2024-10"));
        System.out.println(util.isValid("23:12:45"));
        System.out.println(util.isValid("11:12:45 PM"));
        System.out.println(util.isValid("23:12:45 PM"));
        System.out.println(util.isValid("14:30:00.123456789"));
        System.out.println(util.isValid(""));
        System.out.println(util.isValid(null));
    }

    private static void test_getFormatOf(DateTimeUtils util) {
        System.out.println(util.getFormatOf("2024-12-20 14:30:00"));
        System.out.println(util.getFormatOf("Dec 20, 2024 02:30:00 am"));
        System.out.println(util.getFormatOf("Dec 20, 2024 02:30:00 PM"));
        System.out.println(util.getFormatOf("dec 20, 2024 02:30:00 Pm"));
        System.out.println(util.getFormatOf("Dec 20, 2024 14:30:00"));
        System.out.println(util.getFormatOf("12/20/2024 02:30:00 PM"));
        System.out.println(util.getFormatOf("20241220143000"));
        System.out.println(util.getFormatOf("2024-12-20 14:30:00.1234+05:30"));
        System.out.println(util.getFormatOf("Fri, 20 Dec 2024 14:30:00 +0530"));
        System.out.println(util.getFormatOf("2024 December"));
        System.out.println(util.getFormatOf("2024 Dec"));
        System.out.println(util.getFormatOf("20.12.2024"));
        System.out.println(util.getFormatOf("Fri"));
        System.out.println(util.getFormatOf("Friday"));
        System.out.println(util.getFormatOf("20 Dec 2024"));
        System.out.println(util.getFormatOf("2 December 2024"));
        System.out.println(util.getFormatOf("2024-10"));
        System.out.println(util.getFormatOf("23:12:45"));
        System.out.println(util.getFormatOf("11:12:45 PM"));
        System.out.println(util.getFormatOf("23:12:45 PM"));
        System.out.println(util.getFormatOf("14:30:00.123456789"));
        System.out.println(util.getFormatOf(""));
        System.out.println(util.getFormatOf(null));
    }

    private static void test_toMethods(DateTimeUtils util) {
        System.out.println(util.toLocalDateTime("2024-12-20 14:30:00"));
        System.out.println(util.toLocalDate("Dec 20, 2024 02:30:00 am"));
        System.out.println(util.toDate("12/20/2024 02:30:00 PM"));
        System.out.println(util.toLocalTime("20241220143000"));

        System.out.println(util.toLocalDateTime("2024 Dec"));
        System.out.println(util.toLocalDate("20.12.2024"));
        System.out.println(util.toDate("Fri"));
        System.out.println(util.toLocalTime("Friday"));
        System.out.println(util.getFormatOf("20 Dec 2024"));
        System.out.println(util.getFormatOf(""));
        System.out.println(util.getFormatOf(null));
    }

    private static void test_formatTo(DateTimeUtils util) {
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_OFFSET_DATE_TIME));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_INSTANT));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_ORDINAL_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_WEEK_DATE));

        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_SHORT_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MEDIUM_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_LONG_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_FULL_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_SHORT_DATE_TIME));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_SHORT_DATE_TIME_AM_PM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MEDIUM_DATE_TIME));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MEDIUM_DATE_TIME_am_pm));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MONTH_YEAR));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_US_MONTH_DAY));

        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_SHORT_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_MEDIUM_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_LONG_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_FULL_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_SHORT_DATE_TIME));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_SHORT_DATE_TIME_24));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_DAY_MONTH));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_YEAR_MONTH));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_EURO_YYYYMMDD));

        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_SHORT_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_MEDIUM_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_LONG_DATE));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_BRITISH_FULL_DATE));

        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_JAPANESE_YYYY_MM_DD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_JAPANESE_YYYYMMDD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_JAPANESE_YYMMDD));

        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HHMMSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hhMMSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HHMM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hhMM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_HH_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_hh_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_HH_MM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_hh_MM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM_SS_SSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM_SS_SSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_HH_MM_SS_SSSSSSSSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_hh_MM_SS_SSSSSSSSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_HH_MM_SS_WITH_MILLIS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_TIME_AM_PM_hh_MM_SS_WITH_MILLIS));

        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYYMMDD_HHMMSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYYMMDD_HHMM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_MM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMMM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMM_DD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_MMMM_DD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_D_MMM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_D_MMMM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_DOT_MM_DOT_DD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_DD_DOT_MM_DOT_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_MMM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_COMMON_YYYY_MMMM));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MMM_DD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MMMM_DD));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_HH_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD__HH_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS_X));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_HH_MM_SS_SSS_X));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_YYYY_MM_DD_HH_MM_SS_SSSS_XXX));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMM_YYYY_HH_MM_SS));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMMM_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MMM_COMMA_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DD_MON_COMMA_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_MMM_DD_COMMA_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_MMMM_DD_COMMA_YYYY));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DAY_OF_WEEK_SHORT));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_DAY_OF_WEEK_FULL));
        System.out.println(util.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));
        System.out.println(util.formatTo("", DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));
        System.out.println(util.formatTo(null, DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));
    }
}