package com.github.artemgrishin322.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {

    @NotBlank
    @Size(min = 2, max = 150)
    String address;

    @NotBlank
    @Size(min = 2, max = 150)
    String description;

    public RestaurantTo(Integer id, String name, String address, String description) {
        super(id, name);
        this.address = address;
        this.description = description;
    }
}
