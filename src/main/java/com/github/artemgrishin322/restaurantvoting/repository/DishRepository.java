package com.github.artemgrishin322.restaurantvoting.repository;

import com.github.artemgrishin322.restaurantvoting.model.Dish;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
}
