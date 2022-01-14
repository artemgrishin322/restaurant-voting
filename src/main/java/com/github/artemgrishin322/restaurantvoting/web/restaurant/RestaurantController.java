package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.service.RestaurantService;
import com.github.artemgrishin322.restaurantvoting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "restaurants")
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    @Autowired
    private RestaurantService service;

    @GetMapping
    @Cacheable
    public List<Restaurant> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return ResponseEntity.of(service.get(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        service.vote(authUser.id(), id);
    }
}