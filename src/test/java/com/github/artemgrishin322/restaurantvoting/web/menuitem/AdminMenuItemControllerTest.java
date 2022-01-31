package com.github.artemgrishin322.restaurantvoting.web.menuitem;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import com.github.artemgrishin322.restaurantvoting.repository.MenuItemRepository;
import com.github.artemgrishin322.restaurantvoting.to.MenuItemTo;
import com.github.artemgrishin322.restaurantvoting.util.MenuItemUtil;
import com.github.artemgrishin322.restaurantvoting.util.JsonUtil;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.menuitem.MenuItemTestData.*;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.artemgrishin322.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends AbstractControllerTest {
    private static String getRestUrlForRestaurantId(int restaurantId) {
        return "/api/admin/restaurants/" + restaurantId + "/menus/";
    }

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MENU_ITEM_1, MENU_ITEM_2, MENU_ITEM_3));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllForFirstRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MENU_ITEM_1, MENU_ITEM_2, MENU_ITEM_3, PREVIOUS_MENU_ITEM));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAllForSecondRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT2_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MENU_ITEM_4, MENU_ITEM_5, MENU_ITEM_6));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT2_ID) + MENU_ITEM4_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MENU_ITEM_4));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + MENU_ITEM1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + NOT_FOUND_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getForDate() throws Exception {
        perform(MockMvcRequestBuilders.get(getRestUrlForRestaurantId(RESTAURANT1_ID) + "for-date")
                .param("date", "2019-02-10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(PREVIOUS_MENU_ITEM));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForRestaurantId(RESTAURANT2_ID) + MENU_ITEM4_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuItemRepository.findById(MENU_ITEM4_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(getRestUrlForRestaurantId(RESTAURANT2_ID) + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItemTo newTo = getNewTo(RESTAURANT1_ID);
        MenuItem newMenuItem = MenuItemUtil.createNewFromTo(newTo);
        ResultActions actions = perform(MockMvcRequestBuilders.post(getRestUrlForRestaurantId(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());

        MenuItem created = DISH_MATCHER.readFromJson(actions);
        int newId = created.id();
        newMenuItem.setId(newId);
        DISH_MATCHER.assertMatch(created, newMenuItem);
        DISH_MATCHER.assertMatch(menuItemRepository.getById(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuItemTo newInvalid = new MenuItemTo(null, "", -9870, null, 0);
        perform(MockMvcRequestBuilders.post(getRestUrlForRestaurantId(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newInvalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        MenuItemTo updatedTo = getUpdatedTo(MENU_ITEM5_ID, RESTAURANT2_ID);
        perform(MockMvcRequestBuilders.put(getRestUrlForRestaurantId(RESTAURANT2_ID) + MENU_ITEM5_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(menuItemRepository.getById(MENU_ITEM5_ID), MenuItemUtil.updateFromTo(new MenuItem(MENU_ITEM_5), updatedTo));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuItemTo updatedInvalid = new MenuItemTo(null, "", -9870, null, 0);
        updatedInvalid.setId(MENU_ITEM_1.getId());
        perform(MockMvcRequestBuilders.put(getRestUrlForRestaurantId(RESTAURANT1_ID) + MENU_ITEM1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedInvalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        MenuItemTo updated = getUpdatedTo(MENU_ITEM1_ID, RESTAURANT1_ID);
        updated.setId(MENU_ITEM1_ID);
        updated.setName("<script>alert123</script>");
        perform(MockMvcRequestBuilders.put(getRestUrlForRestaurantId(RESTAURANT1_ID) + MENU_ITEM1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}