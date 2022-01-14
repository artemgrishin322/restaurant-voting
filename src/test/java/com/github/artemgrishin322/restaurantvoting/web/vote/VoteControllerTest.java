package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER2_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_PROFILE_URL = "/api/profile/votes/";
    private static final String REST_RESTAURANT_URL = "/api/restaurants/" + RESTAURANT1_ID + "/votes";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER2_MAIL)
    void getAllMy() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_PROFILE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    void getAllRestaurants() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_RESTAURANT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote));
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_PROFILE_URL + VOTE_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(voteRepository.findById(VOTE_ID).isPresent());
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_PROFILE_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}