package com.github.artemgrishin322.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "registered", "restaurant_id"}, name = "dish_unique_restaurant_created_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @Column(name = "registered", nullable = false, updatable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate registered = LocalDate.now();

    @Column(name = "price")
    @PositiveOrZero
    @NotNull
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    @JsonBackReference
    private Restaurant restaurant;

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice());
    }

    public Dish(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}
