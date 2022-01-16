package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 ORDER BY d.name")
    List<Dish> getAllForRestaurant(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 AND d.registered=?2 ORDER BY d.name")
    List<Dish> getForDate(int restaurantId, LocalDate date);

    Optional<Dish> getByIdAndRestaurantId(int id, int restaurantId);
}
