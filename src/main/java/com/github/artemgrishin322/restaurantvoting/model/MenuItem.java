package com.github.artemgrishin322.restaurantvoting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "serve_date", "name"},
        name = "menu_item_unique_restaurant_name_serve_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class MenuItem extends NamedEntity {

    @Column(name = "serve_date", nullable = false)
    @NotNull
    private LocalDate serveDate;

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
        this(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.getServeDate());
    }

    public MenuItem(Integer id, String name, Integer price, LocalDate serveDate) {
        super(id, name);
        this.price = price;
        this.serveDate = serveDate;
    }
}