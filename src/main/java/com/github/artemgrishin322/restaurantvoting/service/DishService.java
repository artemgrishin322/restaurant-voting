package com.github.artemgrishin322.restaurantvoting.service;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import com.github.artemgrishin322.restaurantvoting.repository.DishRepository;
import com.github.artemgrishin322.restaurantvoting.repository.RestaurantRepository;
import com.github.artemgrishin322.restaurantvoting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;


    public List<Dish> getAllForToday(int restaurantId) {
        return getForDate(restaurantId, null);
    }

    public List<Dish> getAllForRestaurant(int restaurantId) {
        return dishRepository.getAllForRestaurant(restaurantId);
    }

    public Optional<Dish> getForRestaurant(int id, int restaurantId) {
        return dishRepository.getByIdAndRestaurantId(id, restaurantId);
    }

    public void delete(int id) {
        dishRepository.deleteExisted(id);
    }

    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && getForRestaurant(dish.id(), restaurantId).isEmpty()) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(() -> new EntityNotFoundException("No restaurant with id=" + restaurantId)));
        return dishRepository.save(dish);
    }

    public List<Dish> getForDate(int restaurantId, LocalDate date) {
        return dishRepository.getForDate(restaurantId, DateUtil.getDateOrToday(date));
    }
}
