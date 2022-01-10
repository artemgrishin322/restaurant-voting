package com.github.artemgrishin322.restaurantvoting.util;

import java.time.LocalDate;

public class DateUtil {
    public static LocalDate getDateOrToday(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }
}
