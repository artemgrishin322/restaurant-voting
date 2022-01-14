package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER2_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminVoteControllerTest extends AbstractControllerTest {
    private static String getRestUrlForUserId(int userId) {
        return "/api/admin/users/" + userId + "/votes/";
    }

    private static String getRestUrlForRestaurantId(int restaurantId) {
        return "/api/admin/restaurants/" + restaurantId + "/votes/";
    }

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllUsers() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForUserId(USER2_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllRestaurants() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForUserId(USER2_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteUsers() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForUserId(USER2_ID) + VOTE_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(voteRepository.findById(VOTE_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteRestaurants() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForRestaurantId(RESTAURANT1_ID) + VOTE_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(voteRepository.findById(VOTE_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForUserId(USER2_ID) + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}