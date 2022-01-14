package com.github.artemgrishin322.restaurantvoting.web.dish;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.to.DishTo;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class,
            "registered", "restaurant");

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;
    public static final int DISH6_ID = 6;
    public static final int PREVIOUS_ID = 7;
    public static final int NOT_FOUND_ID = 100;

    public static final Dish dish1 = new Dish(DISH1_ID, "Steak with potato", 1000);
    public static final Dish dish2 = new Dish(DISH2_ID, "Mushroom soup", 750);
    public static final Dish dish3 = new Dish(DISH3_ID, "Tiramisu", 620);
    public static final Dish dish4 = new Dish(DISH4_ID, "California", 700);
    public static final Dish dish5 = new Dish(DISH5_ID, "Unai Maki", 850);
    public static final Dish dish6 = new Dish(DISH6_ID, "Philadelphia", 1200);
    public static final Dish previous = new Dish(PREVIOUS_ID, "Previous dish", 700);

    static {
        previous.setRegistered(LocalDate.of(2019, Month.FEBRUARY, 10));
    }

    public static DishTo getNewTo() {
        return new DishTo(null, "New Dish", 1000);
    }

    public static DishTo getUpdatedTo() {
        return new DishTo(null, "Updated Dish", 1500);
    }
}
