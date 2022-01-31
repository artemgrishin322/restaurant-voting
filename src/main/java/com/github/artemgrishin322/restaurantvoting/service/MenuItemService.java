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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.artemgrishin322.restaurantvoting.util.DateTimeUtil.DATE_FORMATTER;
import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class MenuItemService {
    private RestaurantRepository restaurantRepository;
    private MenuItemRepository menuItemRepository;

    public List<MenuItemTo> getAllByRestaurantIdForToday(int restaurantId) {
        return getByDateAndRestaurantId(restaurantId, null);
    }

    public List<MenuItemTo> getAllByRestaurantId(int restaurantId) {
        List<MenuItem> allMenus = menuItemRepository.getAllByRestaurantId(restaurantId);
        checkNotFound(allMenus, "No menus found for restaurantId=" + restaurantId);
        return allMenus
                .stream()
                .map(MenuItemUtil::createFromMenuItem)
                .collect(Collectors.toList());
    }

    public Optional<MenuItemTo> getByIdAndRestaurantId(int id, int restaurantId) {
        Optional<MenuItem> found = menuItemRepository.getByIdAndRestaurantId(id, restaurantId);
        checkNotFound(found, "No menu item with id=" + id + " and restaurantId=" + restaurantId + " found");
        return found.map(MenuItemUtil::createFromMenuItem);
    }

    public void delete(int id, int restaurantId) {
        menuItemRepository.deleteExisted(id, restaurantId);
    }

    @Transactional
    public MenuItemTo save(MenuItem menuItem, int restaurantId) {
        if (!menuItem.isNew()) {
            checkNotFound(getByIdAndRestaurantId(menuItem.id(), restaurantId), "No menu item with id=" + menuItem.id()
                    + " and restaurantId=" + restaurantId + " found");
        } else {
            checkNotFound(restaurantRepository.findById(restaurantId), "No restaurant with id=" + restaurantId + " found");
        }
        menuItem.setRestaurant(restaurantRepository.getById(restaurantId));
        return MenuItemUtil.createFromMenuItem(menuItemRepository.save(menuItem));
    }

    @Transactional
    public List<MenuItemTo> getByDateAndRestaurantId(int restaurantId, LocalDate date) {
        LocalDate dateOrToday = DateTimeUtil.getDateOrToday(date);
        List<MenuItem> todayMenu = menuItemRepository.getByDateAndRestaurantId(restaurantId, dateOrToday);
        checkNotFound(todayMenu, "No menu found for restaurantId=" + restaurantId + " and date " + dateOrToday.format(DATE_FORMATTER));
        return todayMenu.stream()
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