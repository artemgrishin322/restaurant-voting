package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaRestaurantRepository {

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    public DataJpaRestaurantRepository(RestaurantRepository restaurantRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(Sort.by(Sort.Direction.DESC, "name", "address"));
    }

    public Optional<Restaurant> get(int id) {
        return restaurantRepository.findById(id);
    }

    public void delete(int id) {
        restaurantRepository.delete(id);
    }

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void vote(int userId, int restaurantId) {
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
