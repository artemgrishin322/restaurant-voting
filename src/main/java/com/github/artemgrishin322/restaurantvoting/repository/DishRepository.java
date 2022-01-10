package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1")
    List<Dish> getAllForRestaurant(int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    void delete(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 AND d.registered=?2")
    List<Dish> getForDate(int restaurantId, LocalDate date);
}
