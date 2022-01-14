package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.to.RestaurantTo;
import com.github.artemgrishin322.restaurantvoting.util.JsonUtil;
import com.github.artemgrishin322.restaurantvoting.util.RestaurantUtil;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.NOT_FOUND_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.ADMIN_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant2));

    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RESTAURANT1_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        RestaurantTo newTo = getNewTo();
        Restaurant newRestaurant = RestaurantUtil.createNewFromTo(newTo);
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(actions);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createInvalid() throws Exception {
        RestaurantTo newInvalid = new RestaurantTo(null, "", "", "");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newInvalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        RestaurantTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(RESTAURANT1_ID),
                RestaurantUtil.updateFromTo(new Restaurant(restaurant1), updatedTo));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateInvalid() throws Exception {
        RestaurantTo updatedInvalid = new RestaurantTo(null, "", "", "");
        updatedInvalid.setId(RESTAURANT1_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedInvalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        RestaurantTo updated = getUpdatedTo();
        updated.setId(RESTAURANT1_ID);
        updated.setName("<script>alert123</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void vote() throws Exception {
        LocalDate today = LocalDate.now();
        perform(MockMvcRequestBuilders.patch(REST_URL + RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertTrue(voteRepository.getByUserRestaurantAndDate(ADMIN_ID, RESTAURANT2_ID, today).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void voteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}