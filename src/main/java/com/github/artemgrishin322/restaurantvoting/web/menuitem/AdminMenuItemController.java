package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.service.MenuItemService;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.util.MenuItemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class AdminMenuItemController {
    public static final String REST_URL = "api/admin/restaurants/{restaurantId}/dishes";

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        service.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItem> createWithLocation(@RequestBody @Valid MenuItemTo menuItemTo, @PathVariable int restaurantId) {
        Assert.notNull(menuItemTo, "dishTo should not be null");
        log.info("creating {}", menuItemTo);
        checkNew(menuItemTo);
        MenuItem created = service.save(MenuItemUtil.createNewFromTo(menuItemTo), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@RequestBody @Valid MenuItemTo menuItemTo, @PathVariable int id, @PathVariable int restaurantId) {
        Assert.notNull(menuItemTo, "dishTo should not be null");
        assureIdConsistent(menuItemTo, id);
        service.save(MenuItemUtil.createFromTo(menuItemTo), restaurantId);
    }
}
