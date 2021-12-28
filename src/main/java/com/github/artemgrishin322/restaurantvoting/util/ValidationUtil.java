package com.github.artemgrishin322.restaurantvoting.util;

import com.github.artemgrishin322.restaurantvoting.HasId;

public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if(!bean.isNew()) {
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
}