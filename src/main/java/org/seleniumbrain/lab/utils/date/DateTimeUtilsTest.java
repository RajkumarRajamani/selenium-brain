package org.seleniumbrain.lab.utils.date;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


public class DateTimeUtilsTest {

    @Test
    public void testIsValid_withInvalidDates() {
        assertFalse(DateTimeUtils.isValid("2024-13-20 14:30:00")); // Invalid month
        assertFalse(DateTimeUtils.isValid("Dec 32, 2024 02:30:00 PM")); // Invalid day
        assertFalse(DateTimeUtils.isValid("12/20/2024 25:30:00 PM")); // Invalid hour
        assertFalse(DateTimeUtils.isValid("2024-12-20T14:30:00.1234+25:30")); // Invalid offset
        assertFalse(DateTimeUtils.isValid("Invalid Date String"));
        assertFalse(DateTimeUtils.isValid(""));
        assertFalse(DateTimeUtils.isValid(null));
    }

    @Test
    public void testIsValid_withLocale() {
        assertTrue(DateTimeUtils.isValid("20.12.2024", Locale.GERMANY));
        assertTrue(DateTimeUtils.isValid("20/12/2024", Locale.US)); // Invalid format for US locale
    }

    @Test
    public void testGetFormatOf_withValidDates() {
        assertEquals("yyyy-MM-dd", DateTimeUtils.getFormatOf("2024-12-20"));
        assertEquals("MM/dd/yyyy", DateTimeUtils.getFormatOf("12/20/2024"));
        assertEquals("dd.MM.yyyy", DateTimeUtils.getFormatOf("20.12.2024"));
        assertEquals("yyyyMMdd", DateTimeUtils.getFormatOf("20241220"));
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", DateTimeUtils.getFormatOf("2024-12-20T14:30:00"));
        assertEquals("EEE, dd MMM yyyy HH:mm:ss Z", DateTimeUtils.getFormatOf("Fri, 20 Dec 2024 14:30:00 +0530"));
    }

    @Test
    public void testGetFormatOf_withInvalidDates() {
        assertNull(DateTimeUtils.getFormatOf("2024-13-20")); // Invalid month
        assertNull(DateTimeUtils.getFormatOf("Dec 32, 2024")); // Invalid day
        assertNull(DateTimeUtils.getFormatOf("Invalid Date String"));
        assertNull(DateTimeUtils.getFormatOf(""));
        assertNull(DateTimeUtils.getFormatOf(null));
    }

    @Test
    public void testGetFormatOf_withLocale() {
        assertEquals("dd.MM.yyyy", DateTimeUtils.getFormatOf("20.12.2024", Locale.GERMANY));
        assertEquals("MM/dd/yyyy", DateTimeUtils.getFormatOf("12/20/2024", Locale.US));
    }

    @Test
    public void testIsFormatAt_withValidFormat() {
        assertTrue(DateTimeUtils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertTrue(DateTimeUtils.isFormatAt("12/20/2024", DateTimeFormat.FORMAT_US_SHORT_DATE));
        assertTrue(DateTimeUtils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_EURO_SHORT_DATE));
    }

    @Test
    public void testIsFormatAt_withInvalidFormat() {
        assertFalse(DateTimeUtils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_US_SHORT_DATE));
        assertFalse(DateTimeUtils.isFormatAt("12/20/2024", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertFalse(DateTimeUtils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_US_SHORT_DATE));
    }

    @Test
    public void testIsFormatAt_withLocale() {
        assertTrue(DateTimeUtils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY));
        assertFalse(DateTimeUtils.isFormatAt("20/12/2024", DateTimeFormat.FORMAT_US_SHORT_DATE, Locale.US));
    }

    @Test
    public void testIsFormatAt_withStringFormat() {
        assertTrue(DateTimeUtils.isFormatAt("2024-12-20", "yyyy-MM-dd"));
        assertFalse(DateTimeUtils.isFormatAt("12/20/2024", "yyyy-MM-dd"));
    }

    @Test
    public void testIsFormatAt_withStringFormatAndLocale() {
        assertTrue(DateTimeUtils.isFormatAt("20.12.2024", "dd.MM.yyyy", Locale.GERMANY));
        assertFalse(DateTimeUtils.isFormatAt("20/12/2024", "MM/dd/yyyy", Locale.US));
    }

    @Test
    public void testFormatTo_withAllOptions() {
        String dateStr = "12/20/2024 02:30:00 PM";
        DateTimeFormat toFormat = DateTimeFormat.FORMAT_ISO_LOCAL_DATE;
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.US)
                .zoneId(ZoneId.of("America/New_York"))
                .zoneOffset(ZoneOffset.ofHours(-5))
                .build();

        String result = DateTimeUtils.formatTo(dateStr, toFormat, options);
        assertEquals("2024-12-20", result);
    }

    @Test
    public void testFormatTo_withLocaleOnly() {
        String dateStr = "20.12.2024";
        DateTimeFormat toFormat = DateTimeFormat.FORMAT_ISO_LOCAL_DATE;
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.GERMANY)
                .build();

        String result = DateTimeUtils.formatTo(dateStr, toFormat, options);
        assertEquals("2024-12-20", result);
    }

    @Test
    public void testFormatTo_withZoneIdOnly() {
        String dateStr = "2024-12-20T14:30:00";
        DateTimeFormat toFormat = DateTimeFormat.FORMAT_ISO_OFFSET_DATE_TIME;
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .zoneId(ZoneId.of("Europe/Paris"))
                .build();

        String result = DateTimeUtils.formatTo(dateStr, toFormat, options);
        assertEquals("2024-12-20T14:30:00+01:00", result);
    }

    @Test
    public void testFormatTo_withZoneOffsetOnly() {
        String dateStr = "2024-12-20T14:30:00";
        DateTimeFormat toFormat = DateTimeFormat.FORMAT_ISO_OFFSET_DATE_TIME;
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .zoneOffset(ZoneOffset.ofHours(1))
                .build();

        String result = DateTimeUtils.formatTo(dateStr, toFormat, options);
        assertEquals("2024-12-20T14:30:00+05:30", result);
    }

    @Test
    public void testFormatTo_withNoOptions() {
        String dateStr = "12/20/2024 02:30:00 PM";
        DateTimeFormat toFormat = DateTimeFormat.FORMAT_ISO_LOCAL_DATE;

        String result = DateTimeUtils.formatTo(dateStr, toFormat, null);
        assertEquals("2024-12-20", result);
    }

    @Test
    public void testCompareDate() {
        String dateStr1 = "12/20/2024 02:30:00 PM";
        String dateStr2 = "2024-12-20 14:30:00";
        assertTrue(DateTimeUtils.compareDate(dateStr1, dateStr2));
    }

    @Test
    public void testCompareDateTime() {
        String dateStr1 = "12/20/2024 02:30:00 PM";
        String dateStr2 = "2024-12-20 14:30:00";
        assertTrue(DateTimeUtils.compareDateTime(dateStr1, dateStr2));
    }
}