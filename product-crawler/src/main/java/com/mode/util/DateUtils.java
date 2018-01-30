package com.mode.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static long currentTimeMillis() {
        return Instant.now().toEpochMilli();
    }

    // returns ISO 8601 format, e.g. 2014-02-15T01:02:03Z
    public static String toIso8601() {
        return Instant.now().toString();
    }

    // returns ISO 8601 format, e.g. 2014-02-15T01:02:03Z
    public static String toIso8601(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).toString();
    }

    // parsing from ISO 8601 UTC date time - e.g. 2014-02-15T01:02:03Z
    public static long fromIso8601(String dateTime) {
        return Instant.parse(dateTime).toEpochMilli();
    }

    // parsing from ISO 8601 offset date time - "2012-02-15T15:12:21-05:00"
    public static long fromOffsetDateTime(String offsetDateTime) {
        return OffsetDateTime.parse(offsetDateTime).toInstant().toEpochMilli();
    }

    // get year code, year = (currentYear - 2016) % 10
    public static int yearsSince2016() {
        return Period.between(
                LocalDate.of(2016, Month.JANUARY, 1),
                LocalDate.now())
                .getYears() % 10;
    }

    public static String format(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(Instant.now());
    }

    public static String format(Instant instant, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(instant);
    }
}