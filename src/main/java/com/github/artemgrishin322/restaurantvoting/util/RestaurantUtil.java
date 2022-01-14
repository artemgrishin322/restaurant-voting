package com.github.artemgrishin322.restaurantvoting.util;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.to.RestaurantTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestaurantUtil {
    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(null, restaurantTo.getName(), restaurantTo.getAddress(), restaurantTo.getDescription());
    }

    public static Restaurant updateFromTo(Restaurant restaurant, RestaurantTo restaurantTo) {
        restaurant.setName(restaurantTo.getName());
        restaurant.setAddress(restaurantTo.getAddress());
        restaurant.setDescription(restaurantTo.getDescription());
        return restaurant;
    }

    public static Restaurant createFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName(), restaurantTo.getAddress(), restaurantTo.getDescription());
    }
}
