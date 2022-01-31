package com.github.artemgrishin322.restaurantvoting.web.vote;

import com.github.artemgrishin322.restaurantvoting.service.VoteService;
import com.github.artemgrishin322.restaurantvoting.to.VoteTo;
import com.github.artemgrishin322.restaurantvoting.util.DateTimeUtil;
import com.github.artemgrishin322.restaurantvoting.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String REST_URL = "/api/profile/votes";

    private VoteService voteService;

    @GetMapping("/by-date")
    public ResponseEntity<VoteTo> getByUserAndDate(@AuthenticationPrincipal AuthUser authUser, @RequestParam LocalDate voteDate) {
        log.info("getting vote of user id={} by date {}", authUser.id(), voteDate.format(DateTimeUtil.DATE_FORMATTER));
        Optional<VoteTo> found = voteService.getByUserIdAndDate(authUser.id(), voteDate);
        checkNotFound(found, "No vote found for date " + voteDate.format(DateTimeUtil.DATE_FORMATTER));
        return ResponseEntity.of(found);
    }

    @PostMapping
    public ResponseEntity<VoteTo> createWithLocation(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Vote from user id={} for restaurant id={}", authUser.id(), restaurantId);
        VoteTo created = voteService.save(restaurantId, authUser.getUser());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Changing vote from user id={} for restaurant id={}", authUser.id(), restaurantId);
        voteService.save(restaurantId, authUser.getUser());
    }
}