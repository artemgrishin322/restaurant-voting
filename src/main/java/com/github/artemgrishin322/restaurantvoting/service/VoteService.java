package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.User;
import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.to.VoteTo;
import com.github.artemgrishin322.restaurantvoting.util.VoteUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.assureTimeLimitIsNotExceeded;

@Service
@AllArgsConstructor
public class VoteService {
    private VoteRepository voteRepository;
    private RestaurantRepository restaurantRepository;

    @Transactional
    public VoteTo save(int restaurantId, User user) {
        Optional<Vote> existed = voteRepository.getByUserIdAndDate(user.id(), LocalDate.now());
        if (existed.isPresent()) {
            assureTimeLimitIsNotExceeded(LocalTime.now());
        }
        Vote vote = existed.isEmpty() ? new Vote() : existed.get();
        vote.setRestaurant(restaurantRepository.getById(restaurantId));
        vote.setUser(user);
        return VoteUtil.createFromVote(voteRepository.save(vote));
    }

    public Optional<VoteTo> getByUserIdAndDate(int userId, LocalDate voteDate) {
        return voteRepository.getByUserIdAndDate(userId, voteDate).map(VoteUtil::createFromVote);
    }
}