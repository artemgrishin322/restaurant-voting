package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.menuitem.MenuItemTestData.*;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuItemControllerTest extends AbstractControllerTest {
    private static String getRestUrlForRestaurantId(int restaurantId) {
        return "/api/restaurants/" + restaurantId + "/menus/";
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MENU_ITEM_2, MENU_ITEM_1, MENU_ITEM_3));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT2_ID) + MENU_ITEM4_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MENU_ITEM_4));
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
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + MENU_ITEM1_ID))
                .andExpect(status().isUnauthorized());
    }
}