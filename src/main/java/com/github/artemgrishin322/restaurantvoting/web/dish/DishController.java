package com.github.artemgrishin322.restaurantvoting.web.dish;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class DishController {
    public static final String REST_URL = "api/restaurants/{restaurantId}/dishes";

    @Autowired
    private DishService service;

    @GetMapping
    @Cacheable
    public List<Dish> getAllForToday(@PathVariable int restaurantId) {
        log.info("getting today's dishes for restaurant id={}", restaurantId);
        return service.getAllForToday(restaurantId);
    }

    @GetMapping("/all")
    @Cacheable
    public List<Dish> getAllForRestaurant(@PathVariable int restaurantId) {
        log.info("getting all dishes for restaurant id={}", restaurantId);
        return service.getAllForRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getForRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        return ResponseEntity.of(service.getForRestaurant(id, restaurantId));
    }

    @GetMapping("/for-date")
    public List<Dish> getForDate(@PathVariable int restaurantId,
                                 @RequestParam @Nullable LocalDate date) {
        log.info("getting dishes for {} for restaurant id={}", date, restaurantId);
        return service.getForDate(restaurantId ,date);
    }
}
