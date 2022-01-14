package com.github.artemgrishin322.restaurantvoting.web.dish;

import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.dish.DishTestData.*;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractControllerTest {
    private static String getRestUrlForRestaurantId(int restaurantId) {
        return "/api/restaurants/" + restaurantId + "/dishes/";
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish2, dish1, dish3));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish2, dish1, dish3));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT2_ID) + DISH4_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish4));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getForDate() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + "for-date")
                .param("date", "2019-02-10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(previous));
    }
}