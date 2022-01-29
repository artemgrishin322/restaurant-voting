package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.service.MenuItemService;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class MenuItemController {
    public static final String REST_URL = "api/restaurants/{restaurantId}/menus";

    @Autowired
    private MenuItemService service;

    @GetMapping("/api/restaurants/menus")
    public List<MenuItemTo> getAllForAllRestaurantsToday() {
        log.info("getting all menus for all restaurants for today");
        return service.getAllForAllRestaurantsToday();
    }

    @GetMapping(REST_URL + "/today")
    @Cacheable
    public List<MenuItemTo> getAllForRestaurantToday(@PathVariable int restaurantId) {
        log.info("getting today's dishes for restaurant id={}", restaurantId);
        return service.getAllForToday(restaurantId);
    }

    @GetMapping(REST_URL + "/{id}")
    public ResponseEntity<MenuItemTo> getForRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        return ResponseEntity.of(service.getForRestaurant(id, restaurantId));
    }
}