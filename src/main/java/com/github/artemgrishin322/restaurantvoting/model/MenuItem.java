package com.github.artemgrishin322.restaurantvoting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "serve_date", "name"}, name = "dish_unique_restaurant_created_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class MenuItem extends NamedEntity {

    @Column(name = "serve_date", nullable = false, updatable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate serveDate = LocalDate.now();

    @Column(name = "price")
    @PositiveOrZero
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Restaurant restaurant;

    @SuppressWarnings("CopyConstructorMissesField")
    public MenuItem(MenuItem menuItem) {
        this(menuItem.getId(), menuItem.getName(), menuItem.getPrice());
    }

    public MenuItem(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}
