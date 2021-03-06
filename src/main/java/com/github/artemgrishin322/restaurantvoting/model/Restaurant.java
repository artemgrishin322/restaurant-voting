package com.github.artemgrishin322.restaurantvoting.model;

import com.github.artemgrishin322.restaurantvoting.util.validation.NoHtml;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}, name = "restaurant_unique_name_address_idx"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 2, max = 150)
    @NoHtml
    private String address;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 150)
    @NoHtml
    private String description;

    @Column(name = "registered", nullable = false, updatable = false, columnDefinition = "date default now()")
    @NotNull
    private Date registered = new Date();

    public Restaurant(Integer id, String name, String address, String description) {
        super(id, name);
        this.address = address;
        this.description = description;
    }
}