package com.github.artemgrishin322.restaurantvoting.util.validation;

import com.github.artemgrishin322.restaurantvoting.HasId;
import com.github.artemgrishin322.restaurantvoting.error.IllegalRequestDataException;
import com.github.artemgrishin322.restaurantvoting.error.LateVoteException;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.time.LocalTime;

@UtilityClass
public class ValidationUtil {

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

    public static void assureTimeLimitIsNotExceeded(LocalTime timeLimit) {
        if (LocalTime.now().isAfter(timeLimit)) {
            throw new LateVoteException("Too late to vote");
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
}
