package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1 ORDER BY m.serveDate DESC, m.name")
    List<MenuItem> getAllForRestaurant(int restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1 AND m.serveDate=?2 ORDER BY m.serveDate DESC, m.name")
    List<MenuItem> getForDate(int restaurantId, LocalDate date);

    Optional<MenuItem> getByIdAndRestaurantId(int id, int restaurantId);
}
