package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.user2;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);

    public static final int VOTE_ID = 1;
    public static final int NOT_FOUND_ID = 100;

    public static final Vote vote = new Vote(VOTE_ID);

    static {
        vote.setUser(user2);
        vote.setRestaurant(restaurant1);
    }
}
