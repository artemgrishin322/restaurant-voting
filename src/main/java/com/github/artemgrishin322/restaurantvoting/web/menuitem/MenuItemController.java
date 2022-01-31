package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.service.MenuItemService;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@CacheConfig(cacheNames = "menu_items")
public class MenuItemController {
    public static final String REST_URL = "api/restaurants/{restaurantId}/menus";

    private MenuItemService service;

    @GetMapping("/api/restaurants/menus")
    @Cacheable
    public List<MenuItemTo> getAllForAllRestaurantsForToday() {
        log.info("getting all menu items for all restaurants for today");
        return service.getAllForAllRestaurantsForToday();
    }

    @GetMapping(REST_URL + "/today")
    @Cacheable
    public List<MenuItemTo> getAllForRestaurantForToday(@PathVariable int restaurantId) {
        log.info("getting today's menu for restaurant id={}", restaurantId);
        return service.getAllByRestaurantIdForToday(restaurantId);
    }

    @GetMapping(REST_URL + "/{id}")
    public ResponseEntity<MenuItemTo> getForRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("getting menu item with id={} for restaurant id={}", id, restaurantId);
        return ResponseEntity.of(service.getByIdAndRestaurantId(id, restaurantId));
    }
}