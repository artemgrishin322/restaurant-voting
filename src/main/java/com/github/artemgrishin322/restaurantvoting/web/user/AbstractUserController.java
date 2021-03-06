package com.github.artemgrishin322.restaurantvoting.web.user;

import com.github.artemgrishin322.restaurantvoting.model.User;
import com.github.artemgrishin322.restaurantvoting.repository.UserRepository;
import com.github.artemgrishin322.restaurantvoting.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("getting user with id = {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    public void delete(int id) {
        log.info("deleting user with id = {}", id);
        userRepository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return userRepository.save(UserUtil.prepareToSave(user));
    }
}