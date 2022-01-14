package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.NOT_FOUND_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER1_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
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
    void vote() throws Exception {
        LocalDate today = LocalDate.now();
        perform(MockMvcRequestBuilders.patch(REST_URL + RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertTrue(voteRepository.getByUserRestaurantAndDate(USER1_ID, RESTAURANT2_ID, today).isPresent());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void voteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}