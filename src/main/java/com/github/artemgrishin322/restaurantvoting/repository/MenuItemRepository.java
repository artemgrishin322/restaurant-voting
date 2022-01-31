package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.MenuItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.artemgrishin322.restaurantvoting.util.validation.ValidationUtil.checkModification;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1 ORDER BY m.serveDate DESC, m.name")
    List<MenuItem> getAllByRestaurantId(int restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1 AND m.serveDate=?2 ORDER BY m.name")
    List<MenuItem> getByDateAndRestaurantId(int restaurantId, LocalDate date);

    @Query("SELECT m FROM MenuItem m LEFT JOIN  FETCH m.restaurant r WHERE m.serveDate=CURRENT_DATE ORDER BY r.name, m.name")
    List<MenuItem> getAllForAllRestaurantsToday();

    Optional<MenuItem> getByIdAndRestaurantId(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem m WHERE m.id=?1 AND m.restaurant.id=?2")
    int delete(int id, int restaurantId);

    default void deleteExisted(int id, int restaurantId) {
        checkModification(delete(id, restaurantId), id);
    }
}