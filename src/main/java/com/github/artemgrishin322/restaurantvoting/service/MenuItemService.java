package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.repository.MenuItemRepository;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;


    public List<MenuItem> getAllForToday(int restaurantId) {
        return getForDate(restaurantId, null);
    }

    public List<MenuItem> getAllForRestaurant(int restaurantId) {
        return menuItemRepository.getAllForRestaurant(restaurantId);
    }

    public Optional<MenuItem> getForRestaurant(int id, int restaurantId) {
        return menuItemRepository.getByIdAndRestaurantId(id, restaurantId);
    }

    public void delete(int id) {
        menuItemRepository.deleteExisted(id);
    }

    public MenuItem save(MenuItem menuItem, int restaurantId) {
        if (!menuItem.isNew() && getForRestaurant(menuItem.id(), restaurantId).isEmpty()) {
            return null;
        }
        menuItem.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(() -> new EntityNotFoundException("No restaurant with id=" + restaurantId)));
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getForDate(int restaurantId, LocalDate date) {
        return menuItemRepository.getForDate(restaurantId, DateUtil.getDateOrToday(date));
    }
}
