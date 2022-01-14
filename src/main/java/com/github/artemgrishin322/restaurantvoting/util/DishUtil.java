package com.github.artemgrishin322.restaurantvoting.util;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.to.DishTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DishUtil {
    public static Dish createNewFromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), dishTo.getPrice());
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        return dish;
    }

    public static Dish createFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
    }
}
