package com.github.artemgrishin322.restaurantvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"registered", "user_id"}, name = "vote_unique_per_day_idx")})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @Column(name = "registered", nullable = false, updatable = false, columnDefinition = "date default now()")
    LocalDate registered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @ToString.Exclude
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @ToString.Exclude
    Restaurant restaurant;

    public Vote(Integer id) {
        super(id);
    }
}
