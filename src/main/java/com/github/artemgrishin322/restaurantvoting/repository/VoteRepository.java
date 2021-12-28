package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
}
