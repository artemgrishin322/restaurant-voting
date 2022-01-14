package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "votes")
public class AdminVoteController {
    static final String REST_USER_URL = "/api/admin/users/{id}/votes";
    static final String REST_RESTAURANT_URL = "/api/admin/restaurants/{id}/votes";

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping(AdminVoteController.REST_USER_URL)
    public Set<Vote> getAllUsers(@PathVariable int id) {
        return voteRepository.getAllByUserId(id);
    }

    @DeleteMapping(AdminVoteController.REST_USER_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsers(@PathVariable int id) {
        voteRepository.deleteExisted(id);
    }

    @GetMapping(AdminVoteController.REST_RESTAURANT_URL)
    public Set<Vote> getAllRestaurants(@PathVariable int id) {
        return voteRepository.getAllByRestaurantIdForToday(id);
    }

    @DeleteMapping(AdminVoteController.REST_RESTAURANT_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurants(@PathVariable int id) {
        voteRepository.deleteExisted(id);
    }
}
