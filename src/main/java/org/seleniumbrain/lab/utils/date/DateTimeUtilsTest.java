package org.seleniumbrain.lab.utils.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

    private DateTimeUtils utils;

    @BeforeEach
    void setUp() {
        utils = new DateTimeUtils(Locale.US, ZoneId.of("America/New_York"), ZoneOffset.of("-05:00"));
    }

    @Test
    void testIsValid() {
        assertTrue(utils.isValid("2024-12-20 14:30:00"));
        assertFalse(utils.isValid("invalid-date"));
    }

    @Test
    void testIsValidWithLocale() {
        assertTrue(utils.isValid("20.12.2024", Locale.GERMANY));
        assertFalse(utils.isValid("invalid-date", Locale.GERMANY));
    }

    @Test
    void testGetFormatOf() {
        assertEquals("yyyy-MM-dd HH:mm:ss", utils.getFormatOf("2024-12-20 14:30:00"));
        assertNull(utils.getFormatOf("invalid-date"));
    }

    @Test
    void testGetFormatOfWithLocale() {
        assertEquals("dd.MM.yyyy", utils.getFormatOf("20.12.2024", Locale.GERMANY));
        assertNull(utils.getFormatOf("invalid-date", Locale.GERMANY));
    }

    @Test
    void testIsFormatAt() {
        assertTrue(utils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertFalse(utils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
    }

    @Test
    void testIsFormatAtWithLocale() {
        assertTrue(utils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY));
        assertFalse(utils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY));
    }

    @Test
    void testFormatTo() {
        assertEquals("2024-12-20", utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertEquals("invalid-date", utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
    }

    @Test
    void testFormatToWithOptions() {
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.US)
                .zoneId(ZoneId.of("America/New_York"))
                .zoneOffset(ZoneOffset.of("-05:00"))
                .build();
        assertEquals("2024-12-20", utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options));
        assertEquals("invalid-date", utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options));
    }

    @Test
    void testCompareDate() {
        assertTrue(utils.compareDate("2024-12-20", "2024-12-20"));
        assertFalse(utils.compareDate("2024-12-20", "2024-12-21"));
    }

    @Test
    void testCompareDateTime() {
        assertTrue(utils.compareDateTime("2024-12-20T14:30:00", "2024-12-20T14:30:00"));
        assertFalse(utils.compareDateTime("2024-12-20T14:30:00", "2024-12-20T15:30:00"));
    }

    @Test
    void testCompareDateTimeWithOptions() {
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.US)
                .zoneId(ZoneId.of("America/New_York"))
                .zoneOffset(ZoneOffset.of("-05:00"))
                .build();
        assertTrue(utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM", options));
        assertFalse(utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:31:00 PM", options));
    }
}