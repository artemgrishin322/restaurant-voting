package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "votes")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/api/profile/votes")
    public Set<Vote> getAllMy(@AuthenticationPrincipal AuthUser authUser) {
        return voteRepository.getAllByUserId(authUser.id());
    }

    @GetMapping("/api/restaurants/{id}/votes")
    @Cacheable
    public Set<Vote> getAllRestaurants(@PathVariable int id) {
        return voteRepository.getAllByRestaurantIdForToday(id);
    }

    @DeleteMapping("/api/profile/votes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict
    public void delete(@PathVariable int id) {
        voteRepository.deleteExisted(id);
    }
}
