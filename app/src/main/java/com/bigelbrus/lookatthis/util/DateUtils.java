package com.bigelbrus.lookatthis.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

public class DateUtils {
    private static DateTimeFormatter ddMMMMyyyHHMM = new DateTimeFormatterBuilder()
            .appendPattern("dd")
            .appendLiteral(' ')
            .appendPattern("MMMM")
            .appendLiteral(' ')
            .appendPattern("yyyy")
            .appendLiteral(' ')
            .appendPattern("HH")
            .appendLiteral(':')
            .appendPattern("mm")
            .toFormatter();
    private static DateTimeFormatter ddMMyyyy = new DateTimeFormatterBuilder()
            .appendPattern("dd")
            .appendLiteral(' ')
            .appendPattern("MM")
            .appendLiteral(' ')
            .appendPattern("yyyy")
            .toFormatter();

    public static String formatDate(LocalDateTime date) {
        return date.format(ddMMMMyyyHHMM);
    }

    public static LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, ddMMMMyyyHHMM);
    }
}
