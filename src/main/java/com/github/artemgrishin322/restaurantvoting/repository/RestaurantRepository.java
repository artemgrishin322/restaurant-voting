package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @EntityGraph(attributePaths = "dishes", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r JOIN r.dishes d WHERE r.id=?1 AND d.registered=CURRENT_DATE")
    Optional<Restaurant> getWithTodayMenu(int id);
}
