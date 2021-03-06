package com.github.artemgrishin322.restaurantvoting.util;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuItemUtil {
    public static MenuItem createNewFromTo(MenuItemTo menuItemTo) {
        return new MenuItem(null, menuItemTo.getName(), menuItemTo.getPrice(), menuItemTo.getServeDate());
    }

    public static MenuItem updateFromTo(MenuItem menuItem, MenuItemTo menuItemTo) {
        menuItem.setName(menuItemTo.getName());
        menuItem.setPrice(menuItemTo.getPrice());
        menuItem.setServeDate(menuItemTo.getServeDate());
        return menuItem;
    }

    public static MenuItem createFromTo(MenuItemTo menuItemTo) {
        return new MenuItem(menuItemTo.getId(), menuItemTo.getName(), menuItemTo.getPrice(), menuItemTo.getServeDate());
    }

    @SuppressWarnings("ConstantConditions")
    public static MenuItemTo createFromMenuItem(MenuItem menuItem) {
        return new MenuItemTo(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.getServeDate(), menuItem.getRestaurant().getId());
    }
}