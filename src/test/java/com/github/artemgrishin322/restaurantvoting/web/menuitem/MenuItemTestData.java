package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class,
            "serveDate", "restaurant");

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;
    public static final int DISH6_ID = 6;
    public static final int PREVIOUS_ID = 7;
    public static final int NOT_FOUND_ID = 100;

    public static final MenuItem MENU_ITEM_1 = new MenuItem(DISH1_ID, "Steak with potato", 1000);
    public static final MenuItem MENU_ITEM_2 = new MenuItem(DISH2_ID, "Mushroom soup", 750);
    public static final MenuItem MENU_ITEM_3 = new MenuItem(DISH3_ID, "Tiramisu", 620);
    public static final MenuItem MENU_ITEM_4 = new MenuItem(DISH4_ID, "California", 700);
    public static final MenuItem MENU_ITEM_5 = new MenuItem(DISH5_ID, "Unai Maki", 850);
    public static final MenuItem MENU_ITEM_6 = new MenuItem(DISH6_ID, "Philadelphia", 1200);
    public static final MenuItem previous = new MenuItem(PREVIOUS_ID, "Previous menu item", 700);

    static {
        previous.setServeDate(LocalDate.of(2019, Month.FEBRUARY, 10));
    }

    public static MenuItemTo getNewTo() {
        return new MenuItemTo(null, "New Dish", 1000);
    }

    public static MenuItemTo getUpdatedTo() {
        return new MenuItemTo(null, "Updated Dish", 1500);
    }
}
