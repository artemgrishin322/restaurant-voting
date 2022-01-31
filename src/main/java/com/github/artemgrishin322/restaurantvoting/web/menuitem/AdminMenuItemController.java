package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.service.MenuItemService;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.util.MenuItemUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "dishes")
public class AdminMenuItemController {
    public static final String REST_URL = "api/admin/restaurants/{restaurantId}/menus";

    private MenuItemService service;

    @GetMapping()
    @Cacheable
    public List<MenuItemTo> getAllForRestaurant(@PathVariable int restaurantId) {
        log.info("getting all menu items for restaurant id={}", restaurantId);
        return service.getAllByRestaurantId(restaurantId);
    }

    @GetMapping("/today")
    @Cacheable
    public List<MenuItemTo> getAllForToday(@PathVariable int restaurantId) {
        log.info("getting today's menu for restaurant id={}", restaurantId);
        return service.getAllByRestaurantIdForToday(restaurantId);
    }

    @GetMapping("/for-date")
    @Cacheable
    public List<MenuItemTo> getByDate(@PathVariable int restaurantId,
                                      @RequestParam @Nullable LocalDate date) {
        log.info("getting menu for {} for restaurant id={}", date, restaurantId);
        return service.getByDateAndRestaurantId(restaurantId ,date);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemTo> getForRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("getting menu item with id={} for restaurant id={}", id, restaurantId);
        return ResponseEntity.of(service.getByIdAndRestaurantId(id, restaurantId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("deleting menu item with id={} for restaurant id={}", id, restaurantId);
        service.delete(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItemTo> createWithLocation(@RequestBody @Valid MenuItemTo menuItemTo, @PathVariable int restaurantId) {
        log.info("creating {} for restaurant id={}", menuItemTo, restaurantId);
        checkNew(menuItemTo);
        MenuItemTo created = service.save(MenuItemUtil.createFromTo(menuItemTo), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@RequestBody @Valid MenuItemTo menuItemTo, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("updating {} for restaurant id={}", menuItemTo, restaurantId);
        assureIdConsistent(menuItemTo, id);
        service.save(MenuItemUtil.createFromTo(menuItemTo), restaurantId);
    }
}