package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.repository.UserRepository;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static com.github.artemgrishin322.restaurantvoting.util.ValidationUtil.assureTimeLimitIsNotExceeded;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected VoteRepository voteRepository;

    @Autowired
    protected UserRepository userRepository;

    protected List<Restaurant> getAll() {
        log.info("getting all restaurants");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.DESC, "name", "address"));
    }

    protected ResponseEntity<Restaurant> get(int id) {
        log.info("getting restaurant with id = {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    protected void vote(int userId, int restaurantId) {
        //TODO rework time limits for testing
//        assureTimeLimitIsNotExceeded();
        Vote vote = voteRepository.getByUser(userId, LocalDate.now()).orElse(null);
        if (vote == null) {
            Vote newVote = new Vote();
            newVote.setUser(userRepository.findById(userId).get());
            newVote.setRestaurant(restaurantRepository.findById(restaurantId).get());
            voteRepository.save(newVote);
        } else {
            vote.setRestaurant(restaurantRepository.findById(restaurantId).get());
            voteRepository.save(vote);
        }
    }
}
