package com.github.artemgrishin322.restaurantvoting.web.user;

import com.github.artemgrishin322.restaurantvoting.model.Role;
import com.github.artemgrishin322.restaurantvoting.model.User;
import com.github.artemgrishin322.restaurantvoting.util.JsonUtil;
import com.github.artemgrishin322.restaurantvoting.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class,
            "registered", "password", "votes");

    public static final int USER1_ID = 1;
    public static final int USER2_ID = 2;
    public static final int ADMIN_ID = 3;
    public static final int NOT_FOUND_ID = 100;
    public static final String USER1_MAIL = "user1@gmail.com";
    public static final String USER2_MAIL = "user2@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";


    public static final User user1 = new User(USER1_ID, "User_1", USER1_MAIL, "password", Role.USER);
    public static final User user2 = new User(USER2_ID, "User_2", USER2_MAIL, "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPassword", false, new Date(),
                Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER1_ID, "UpdatedName", USER1_MAIL, "newPassword", false, new Date(),
                Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}