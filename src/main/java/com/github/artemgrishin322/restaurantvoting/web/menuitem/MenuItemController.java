package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.service.MenuItemService;
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
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class MenuItemController {
    public static final String REST_URL = "api/restaurants/{restaurantId}/dishes";

    @Autowired
    private MenuItemService service;

    @GetMapping
    @Cacheable
    public List<MenuItem> getAllForToday(@PathVariable int restaurantId) {
        log.info("getting today's dishes for restaurant id={}", restaurantId);
        return service.getAllForToday(restaurantId);
    }

    @GetMapping("/all")
    @Cacheable
    public List<MenuItem> getAllForRestaurant(@PathVariable int restaurantId) {
        log.info("getting all dishes for restaurant id={}", restaurantId);
        return service.getAllForRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getForRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        return ResponseEntity.of(service.getForRestaurant(id, restaurantId));
    }

    @GetMapping("/for-date")
    @Cacheable
    public List<MenuItem> getForDate(@PathVariable int restaurantId,
                                     @RequestParam @Nullable LocalDate date) {
        log.info("getting dishes for {} for restaurant id={}", date, restaurantId);
        return service.getForDate(restaurantId ,date);
    }
}
