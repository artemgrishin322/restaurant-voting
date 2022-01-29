package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.repository.UserRepository;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.assureTimeLimitIsNotExceeded;

@Service
public class RestaurantService {
    private static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    @Autowired
    private Environment environment;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "address"));
    }

    public Optional<Restaurant> get(int id) {
        return restaurantRepository.findById(id);
    }

    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
    }

    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant should not be null");
        return restaurantRepository.save(restaurant);
    }

    public void vote(int userId, int restaurantId) {
        LocalTime actualLimit = Arrays.asList(environment.getActiveProfiles()).contains("test") ?
                LocalTime.of(23, 59) : TIME_LIMIT;
        assureTimeLimitIsNotExceeded(actualLimit);
        Vote vote = voteRepository.getByUserAndDate(userId, LocalDate.now()).orElse(null);
        if (vote == null) {
            Vote newVote = new Vote();
            newVote.setUser(userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
            newVote.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(EntityNotFoundException::new));
            voteRepository.save(newVote);
        } else {
            vote.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(EntityNotFoundException::new));
            voteRepository.save(vote);
        }
    }
}
