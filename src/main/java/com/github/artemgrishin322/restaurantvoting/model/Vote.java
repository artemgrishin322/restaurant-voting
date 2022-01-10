package com.github.artemgrishin322.restaurantvoting.model;

import lombok.*;

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
    private LocalDate registered = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @ToString.Exclude
    private Restaurant restaurant;

    public Vote(Integer id) {
        super(id);
    }
}
