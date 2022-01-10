package com.github.artemgrishin322.restaurantvoting.util;

import com.github.artemgrishin322.restaurantvoting.HasId;
import com.github.artemgrishin322.restaurantvoting.error.IllegalRequestDataException;

import java.time.LocalTime;

public class ValidationUtil {

    public static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean.getClass().getSimpleName() + " must be new (id = null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalArgumentException(bean.getClass().getSimpleName() + " must have id = " + id);
        }
    }

    public static void assureTimeLimitIsNotExceeded() {
        if (LocalTime.now().isAfter(TIME_LIMIT)) {
            //TODO change to appropriate exception type
            throw new IllegalArgumentException("Too late to vote");
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }
}
