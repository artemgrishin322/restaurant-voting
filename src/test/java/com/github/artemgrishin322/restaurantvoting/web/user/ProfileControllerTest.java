package com.github.artemgrishin322.restaurantvoting.web.user;

import com.github.artemgrishin322.restaurantvoting.model.User;
import com.github.artemgrishin322.restaurantvoting.repository.UserRepository;
import com.github.artemgrishin322.restaurantvoting.to.UserTo;
import com.github.artemgrishin322.restaurantvoting.util.JsonUtil;
import com.github.artemgrishin322.restaurantvoting.util.UserUtil;
import com.github.artemgrishin322.restaurantvoting.web.AbstractControllerTest;
import com.github.artemgrishin322.restaurantvoting.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.artemgrishin322.restaurantvoting.web.user.ProfileController.REST_URL;
import static com.github.artemgrishin322.restaurantvoting.web.user.UserTestData.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), user2, admin);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@gmail.com", "newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, "", "notValidEmail", "");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "updatedName", USER1_MAIL, "updatedPassword");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getById(USER1_ID), UserUtil.updateFromTo(new User(user1), updatedTo));
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, "", "NotValidEmail", "");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(USER1_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "updatedName", USER2_MAIL, "updatedPassword");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}