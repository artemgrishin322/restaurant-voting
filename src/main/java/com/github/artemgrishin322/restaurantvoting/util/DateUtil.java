package com.github.artemgrishin322.restaurantvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class DateUtil {
    public static LocalDate getDateOrToday(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }
}
