package com.github.artemgrishin322.restaurantvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class DateTimeUtil {
    private static LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    public static LocalDate getDateOrToday(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }

    public static void setTimeLimit(LocalTime newLimit) {
        TIME_LIMIT = newLimit;
    }

    public static LocalTime getTimeLimit() {
        return TIME_LIMIT;
    }
}