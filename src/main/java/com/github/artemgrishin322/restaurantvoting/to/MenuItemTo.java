package com.github.artemgrishin322.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuItemTo extends NamedTo {

    @NotNull
    @PositiveOrZero
    Integer price;

    public MenuItemTo(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}
