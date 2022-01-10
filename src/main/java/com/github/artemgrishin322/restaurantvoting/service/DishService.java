package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.repository.DishRepository;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.util.DateUtil;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DishService {

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllForRestaurant(int restaurantId) {
        return dishRepository.getAllForRestaurant(restaurantId);
    }

    public Optional<Dish> getForRestaurant(int id, int restaurantId) {
        return dishRepository.findById(id)
                .filter(dish -> dish.getRestaurant().getId() == restaurantId);
    }

    public void delete(int id, int restaurantId) {
        dishRepository.delete(id, restaurantId);
    }

    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && getForRestaurant(dish.id(), restaurantId).isEmpty()) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
        return dishRepository.save(dish);
    }

    public List<Dish> getForDate(int restaurantId, LocalDate date) {
        return dishRepository.getForDate(restaurantId, DateUtil.getDateOrToday(date));
    }
}
