package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class,
            "registered", "dishes", "votes");

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int NOT_FOUND_ID = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Mon blah",
            "Moscow, Temiryazevskaya st, p. 64", "French restaurant");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Yanomamo",
            "New York, Broadway, p. 865", "Sushi bar");

    public static Restaurant getNew() {
        return new Restaurant(null, "Brand New Restaurant", "Saint-Petersburg, Nevskaya st., building 17",
                "Absolutely new restaurant with very handsome description");
    }

    public static Restaurant getUpdated(int id) {
        return new Restaurant(id, "Updated restaurant", "Moscow, Red Square, building 258",
                "Updated description");
    }
}