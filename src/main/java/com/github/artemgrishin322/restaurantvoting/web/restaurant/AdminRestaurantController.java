package com.github.artemgrishin322.restaurantvoting.web.restaurant;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import com.github.artemgrishin322.restaurantvoting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.artemgrishin322.restaurantvoting.util.ValidationUtil.assureIdConsistent;
import static com.github.artemgrishin322.restaurantvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "restaurants")
@Slf4j
public class AdminRestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    @GetMapping
    @Cacheable
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("deleting restaurant with id = {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("creating {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("updating {} with id = {}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void vote(@AuthenticationPrincipal AuthUser user, @PathVariable int id) {
        super.vote(user.id(), id);
    }
}
