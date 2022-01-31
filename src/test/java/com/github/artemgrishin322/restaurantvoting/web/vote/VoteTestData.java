package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.to.VoteTo;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;

import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static final int VOTE_TO1_ID = 1;
    public static final int VOTE_TO2_ID = 2;
    public static final int VOTE_TO3_ID = 3;
    public static final int VOTE_TO4_ID = 4;
    public static final int VOTE_TO5_ID = 5;

    public static final LocalDate VOTE2_DATE = LocalDate.of(2021, Month.DECEMBER, 31);

    public static final VoteTo vote1 = new VoteTo(VOTE_TO1_ID, LocalDate.now(), RESTAURANT2_ID);
    public static final VoteTo vote2 = new VoteTo(VOTE_TO2_ID, VOTE2_DATE, RESTAURANT2_ID);
    public static final VoteTo vote3 = new VoteTo(VOTE_TO3_ID, LocalDate.of(2021, Month.OCTOBER, 25), RESTAURANT1_ID);
    public static final VoteTo vote4 = new VoteTo(VOTE_TO4_ID, LocalDate.of(2021, Month.AUGUST, 15), RESTAURANT2_ID);
    public static final VoteTo vote5 = new VoteTo(VOTE_TO5_ID, LocalDate.of(2020, Month.FEBRUARY, 13), RESTAURANT1_ID);
}