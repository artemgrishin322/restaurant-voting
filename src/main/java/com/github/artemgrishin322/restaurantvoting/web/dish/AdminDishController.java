package com.github.artemgrishin322.restaurantvoting.web.dish;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.repository.DataJpaDishRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.artemgrishin322.restaurantvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class AdminDishController {

    static final String REST_URL = "api/admin/restaurants/{restaurantId}/dishes";

    @Autowired
    private DataJpaDishRepository repository;

    @GetMapping
    @Cacheable
    public List<Dish> getAllForRestaurant(@PathVariable int restaurantId) {
        return repository.getAllForRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getForRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        return ResponseEntity.of(repository.getForRestaurant(id, restaurantId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("deleting dish with id = {}", id);
        repository.delete(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("creating {}", dish);
        checkNew(dish);
        Dish created = repository.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("updating {} with id = {}", dish, id);
        assureIdConsistent(dish, id);
        repository.save(dish, restaurantId);
    }

    //TODO getTodayMenu
    //TODO getForDate
}
