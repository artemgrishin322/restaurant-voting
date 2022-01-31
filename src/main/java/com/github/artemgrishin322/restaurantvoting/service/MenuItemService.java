package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.repository.MenuItemRepository;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.util.DateTimeUtil;
import com.github.artemgrishin322.restaurantvoting.util.MenuItemUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MenuItemService {
    private RestaurantRepository restaurantRepository;
    private MenuItemRepository menuItemRepository;


    public List<MenuItemTo> getAllByRestaurantIdForToday(int restaurantId) {
        return getByDateAndRestaurantId(restaurantId, null);
    }

    public List<MenuItemTo> getAllByRestaurantId(int restaurantId) {
        return menuItemRepository.getAllByRestaurantId(restaurantId)
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }

    public Optional<MenuItemTo> getByIdAndRestaurantId(int id, int restaurantId) {
        return menuItemRepository.getByIdAndRestaurantId(id, restaurantId).map(MenuItemUtil::createFromMenuItem);
    }

    public void delete(int id, int restaurantId) {
        menuItemRepository.deleteExisted(id, restaurantId);
    }

    @Transactional
    public MenuItemTo save(MenuItem menuItem, int restaurantId) {
        if (!menuItem.isNew()) {
            getByIdAndRestaurantId(menuItem.id(), restaurantId).orElseThrow(() -> new EntityNotFoundException("Entity with id=" + menuItem.id()
                    + " and restaurantId=" + restaurantId+ " not found"));
        }
        menuItem.setRestaurant(restaurantRepository.getById(restaurantId));
        return MenuItemUtil.createFromMenuItem(menuItemRepository.save(menuItem));
    }

    public List<MenuItemTo> getByDateAndRestaurantId(int restaurantId, LocalDate date) {
        return menuItemRepository.getByDateAndRestaurantId(restaurantId, DateTimeUtil.getDateOrToday(date))
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }

    public List<MenuItemTo> getAllForAllRestaurantsForToday() {
        return menuItemRepository.getAllForAllRestaurantsToday()
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }
}