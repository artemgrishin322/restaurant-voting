package com.github.artemgrishin322.restaurantvoting;

import org.springframework.util.Assert;

public interface HasId {
    Integer getId();

    void setId(Integer id);

    default boolean isNew() {
        return getId() == null;
    }

    default int id() {
        Assert.notNull(getId(), "Entity mist have an id");
        return getId();
    }
}
