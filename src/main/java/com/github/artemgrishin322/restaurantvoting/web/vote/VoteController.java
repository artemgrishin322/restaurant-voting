package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import com.github.artemgrishin322.restaurantvoting.repository.VoteRepository;
import com.github.artemgrishin322.restaurantvoting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "votes")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/api/profile/votes")
    public List<Vote> getAllMy(@AuthenticationPrincipal AuthUser authUser) {
        return voteRepository.getAllByUserId(authUser.id());
    }
}