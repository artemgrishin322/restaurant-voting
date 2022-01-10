package com.github.artemgrishin322.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}, name = "restaurant_unique_address_idx"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    private String address;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 150)
    private String description;

    @Column(name = "registered", nullable = false, updatable = false, columnDefinition = "date default now()")
    @NotNull
    private Date registered = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("registered DESC")
    @ToString.Exclude
    @JsonManagedReference
    private List<Dish> dishes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @ToString.Exclude
    private Set<Vote> votes;

    public Restaurant(Integer id, String name, String address, String description) {
        super(id, name);
        this.address = address;
        this.description = description;
    }
}
