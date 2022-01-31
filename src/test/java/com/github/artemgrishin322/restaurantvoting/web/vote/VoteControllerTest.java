package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.to.VoteTo;
import com.github.artemgrishin322.restaurantvoting.util.DateTimeUtil;
import com.github.artemgrishin322.restaurantvoting.util.VoteUtil;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.*;
import static com.github.artemgrishin322.restaurantvoting.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/profile/votes/";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date")
                .param("voteDate", "2021-12-31"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(vote2));
    }


    @Test
    @WithUserDetails(USER1_MAIL)
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void createWithLocation() throws Exception {
        LocalDate voteDate = LocalDate.now();
        VoteTo newVote = new VoteTo(null, voteDate, RESTAURANT1_ID);
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID)))
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(actions);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_TO_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createFromVote(voteRepository.getByUserIdAndDate(USER1_ID, voteDate).get()), newVote);
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void update() throws Exception {
        DateTimeUtil.setTimeLimit(LocalTime.now().plusSeconds(10));
        LocalDate voteDate = LocalDate.now();
        VoteTo updated = new VoteTo(vote1.id(), voteDate, RESTAURANT2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isNoContent());

        VOTE_TO_MATCHER.assertMatch(VoteUtil.createFromVote(voteRepository.getByUserIdAndDate(USER2_ID, voteDate).get()), updated);
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    void updateAfterDeadline() throws Exception {
        DateTimeUtil.setTimeLimit(LocalTime.now().minusSeconds(10));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isConflict());
    }
}