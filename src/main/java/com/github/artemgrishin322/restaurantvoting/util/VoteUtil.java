package com.github.artemgrishin322.restaurantvoting.util;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.to.VoteTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VoteUtil {

    @SuppressWarnings("ConstantConditions")
    public static VoteTo createFromVote(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVoteDate(), vote.getRestaurant().getId());
    }
}