package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class,
            "serveDate", "restaurant");

    public static final int MENU_ITEM1_ID = 1;
    public static final int MENU_ITEM2_ID = 2;
    public static final int MENU_ITEM3_ID = 3;
    public static final int MENU_ITEM4_ID = 4;
    public static final int MENU_ITEM5_ID = 5;
    public static final int MENU_ITEM6_ID = 6;
    public static final int PREVIOUS_MENU_ITEM_ID = 7;
    public static final int NOT_FOUND_ID = 100;

    public static final MenuItem MENU_ITEM_1 = new MenuItem(MENU_ITEM1_ID, "Steak with potato", 1000, LocalDate.now());
    public static final MenuItem MENU_ITEM_2 = new MenuItem(MENU_ITEM2_ID, "Mushroom soup", 750, LocalDate.now());
    public static final MenuItem MENU_ITEM_3 = new MenuItem(MENU_ITEM3_ID, "Tiramisu", 620, LocalDate.now());
    public static final MenuItem MENU_ITEM_4 = new MenuItem(MENU_ITEM4_ID, "California", 700, LocalDate.now());
    public static final MenuItem MENU_ITEM_5 = new MenuItem(MENU_ITEM5_ID, "Unai Maki", 850, LocalDate.now());
    public static final MenuItem MENU_ITEM_6 = new MenuItem(MENU_ITEM6_ID, "Philadelphia", 1200, LocalDate.now());
    public static final MenuItem PREVIOUS_MENU_ITEM = new MenuItem(PREVIOUS_MENU_ITEM_ID, "Previous menu item", 700, LocalDate.now());

    static {
        PREVIOUS_MENU_ITEM.setServeDate(LocalDate.of(2019, Month.FEBRUARY, 10));
    }

    public static MenuItemTo getNewTo(int restaurantId) {
        return new MenuItemTo(null, "New Dish", 1000, LocalDate.now(), restaurantId);
    }

    public static MenuItemTo getUpdatedTo(int id, int restaurantId) {
        return new MenuItemTo(id, "Updated Dish", 1500, LocalDate.now(), restaurantId);
    }
}