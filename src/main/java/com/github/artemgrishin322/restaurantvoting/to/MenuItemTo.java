package com.github.artemgrishin322.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuItemTo extends NamedTo {

    @PositiveOrZero
    int price;

    @NotNull
    LocalDate serveDate;

    int restaurantId;

    public MenuItemTo(Integer id, String name, int price, LocalDate serveDate, int restaurantId) {
        super(id, name);
        this.price = price;
        this.serveDate = serveDate;
        this.restaurantId = restaurantId;
    }
}