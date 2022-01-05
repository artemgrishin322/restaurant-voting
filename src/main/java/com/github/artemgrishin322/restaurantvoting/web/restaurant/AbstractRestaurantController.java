package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.repository.DataJpaRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.github.artemgrishin322.restaurantvoting.util.ValidationUtil.assureTimeLimitIsNotExceeded;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected DataJpaRestaurantRepository repository;

    protected List<Restaurant> getAll() {
        log.info("getting all restaurants");
        return repository.getAll();
    }

    protected ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("getting restaurant with id = {}", id);
        return ResponseEntity.of(repository.get(id));
    }

    protected void vote(int userId, int restaurantId) {
        assureTimeLimitIsNotExceeded();
        repository.vote(userId, restaurantId);
    }
}
