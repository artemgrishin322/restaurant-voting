package com.github.artemgrishin322.restaurantvoting.web.dish;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.repository.DishRepository;
import com.github.artemgrishin322.restaurantvoting.to.DishTo;
import com.github.artemgrishin322.restaurantvoting.util.DishUtil;
import com.github.artemgrishin322.restaurantvoting.util.JsonUtil;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.dish.DishTestData.*;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {
    private static String getRestUrlForRestaurantId(int restaurantId) {
        return "/api/admin/restaurants/" + restaurantId + "/dishes/";
    }

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish2, dish1, dish3));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllForFirstRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + "all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish2, previous, dish1, dish3));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllForSecondRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT2_ID) + "all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish4, dish6, dish5));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT2_ID) + DISH4_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish4));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
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
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + NOT_FOUND_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getForDate() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + "for-date")
                .param("date", "2019-02-10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(previous));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForRestaurantId(RESTAURANT2_ID) + DISH4_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findById(DISH4_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForRestaurantId(RESTAURANT2_ID) + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        DishTo newTo = getNewTo();
        Dish newDish = DishUtil.createNewFromTo(newTo);
        ResultActions actions = perform(MockMvcRequestBuilders.post(getRestUrlForRestaurantId(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());

        Dish created = DISH_MATCHER.readFromJson(actions);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.getById(newId), newDish);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createInvalid() throws Exception {
        DishTo newInvalid = new DishTo(null, "", -9870);
        perform(MockMvcRequestBuilders.post(getRestUrlForRestaurantId(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newInvalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        DishTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(getRestUrlForRestaurantId(RESTAURANT2_ID) + DISH5_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.getById(DISH5_ID), DishUtil.updateFromTo(new Dish(dish5), updatedTo));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateInvalid() throws Exception {
        DishTo updatedInvalid = new DishTo(null, "", -9870);
        updatedInvalid.setId(dish1.getId());
        perform(MockMvcRequestBuilders.put(getRestUrlForRestaurantId(RESTAURANT1_ID) + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedInvalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        DishTo updated = getUpdatedTo();
        updated.setId(DISH1_ID);
        updated.setName("<script>alert123</script>");
        perform(MockMvcRequestBuilders.put(getRestUrlForRestaurantId(RESTAURANT1_ID) + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}