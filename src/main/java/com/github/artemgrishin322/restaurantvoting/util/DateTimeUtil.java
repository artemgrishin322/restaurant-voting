package com.github.artemgrishin322.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static LocalTime timeLimit = LocalTime.of(11, 0);

    public static LocalDate getDateOrToday(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }

    @Profile("test")
    public static void setTimeLimit(LocalTime newLimit) {
        timeLimit = newLimit;
    }

    public static LocalTime getTimeLimit() {
        return timeLimit;
    }
}