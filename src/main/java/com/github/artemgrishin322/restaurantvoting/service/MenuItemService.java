package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.repository.MenuItemRepository;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.util.DateUtil;
import com.github.artemgrishin322.restaurantvoting.util.MenuItemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;


    public List<MenuItemTo> getAllForToday(int restaurantId) {
        return getForDate(restaurantId, null);
    }

    public List<MenuItemTo> getAllForRestaurant(int restaurantId) {
        return menuItemRepository.getAllForRestaurant(restaurantId)
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }

    public Optional<MenuItemTo> getForRestaurant(int id, int restaurantId) {
        return menuItemRepository.getByIdAndRestaurantId(id, restaurantId).map(MenuItemUtil::createFromMenuItem);
    }

    public void delete(int id, int restaurantId) {
        menuItemRepository.deleteExisted(id, restaurantId);
    }

    @Transactional
    public MenuItemTo save(MenuItem menuItem, int restaurantId) {
        if (!menuItem.isNew()) {
            getForRestaurant(menuItem.id(), restaurantId).orElseThrow(() -> new EntityNotFoundException("Entity with id=" + menuItem.id()
                    + " and restaurantId=" + restaurantId+ " not found"));
        }
        menuItem.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(() -> new EntityNotFoundException("No restaurant with id=" + restaurantId)));
        return MenuItemUtil.createFromMenuItem(menuItemRepository.save(menuItem));
    }

    public List<MenuItemTo> getForDate(int restaurantId, LocalDate date) {
        return menuItemRepository.getForDate(restaurantId, DateUtil.getDateOrToday(date))
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }

    public List<MenuItemTo> getAllForAllRestaurantsToday() {
        return menuItemRepository.getAllForAllRestaurantsToday()
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }
}