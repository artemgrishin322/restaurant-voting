package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaDishRepository {

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public DataJpaDishRepository(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
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
}
