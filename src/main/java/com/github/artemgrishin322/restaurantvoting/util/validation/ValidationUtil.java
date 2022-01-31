package com.github.artemgrishin322.restaurantvoting.util.validation;

import com.github.artemgrishin322.restaurantvoting.HasId;
import com.github.artemgrishin322.restaurantvoting.error.IllegalRequestDataException;
import com.github.artemgrishin322.restaurantvoting.error.TooLateToChangeVoteException;
import com.github.artemgrishin322.restaurantvoting.util.DateTimeUtil;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.artemgrishin322.restaurantvoting.util.DateTimeUtil.TIME_FORMATTER;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id = null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must have id = " + id);
        }
    }

    public static void assureTimeLimitIsNotExceeded(LocalTime currentTime) {
        if (currentTime.isAfter(DateTimeUtil.getTimeLimit())) {
            throw new TooLateToChangeVoteException("To late to change vote, current time is " + currentTime.format(TIME_FORMATTER) + ", time limit is " + DateTimeUtil.getTimeLimit());
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    @NonNull
    public static Throwable getRootCause(Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static <T> void checkNotFound(List<T> listOfFoundItems, String errorMessage) {
        if (listOfFoundItems.isEmpty()) {
            throw new EntityNotFoundException(errorMessage);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> void checkNotFound(Optional<T> foundItem, String errorMessage) {
        if (foundItem.isEmpty()) {
            throw new EntityNotFoundException(errorMessage);
        }
    }
}