package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.voteDate=?2")
    Optional<Vote> getByUserAndDate(int userId, LocalDate today);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.restaurant.id=?2 AND v.voteDate=?3")
    Optional<Vote> getByUserRestaurantAndDate(int userId, int restaurantId, LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant r WHERE v.user.id=?1 ORDER BY v.voteDate DESC")
    List<Vote> getAllByUserId(int id);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=?1 AND v.voteDate=CURRENT_DATE ORDER BY v.voteDate DESC")
    List<Vote> getAllByRestaurantIdForToday(int id);
}