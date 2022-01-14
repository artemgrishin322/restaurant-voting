package com.github.artemgrishin322.restaurantvoting.to;

import com.github.artemgrishin322.restaurantvoting.HasId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class BaseTo implements HasId {
    protected Integer id;

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}
