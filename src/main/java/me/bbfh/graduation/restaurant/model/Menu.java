package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.bbfh.graduation.common.model.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, doNotUseGetters = true)
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "relevancy_date"}, name = "menu_unique_restaurant_date_idx")})
public class Menu extends BaseEntity {

    @ToString.Exclude
    @NotNull
    @Column(name = "relevancy_date", nullable = false, columnDefinition = "date")
    private LocalDate relevancyDate;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @ToString.Exclude
    @NotNull
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Dish> dishes = new HashSet<>();

    public Menu(Integer id, LocalDate relevancyDate, Restaurant restaurant, Set<Dish> dishes) {
        super(id);
        this.relevancyDate = relevancyDate;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Menu(Integer id, LocalDate relevancyDate, Restaurant restaurant) {
        this(id, relevancyDate, restaurant, new HashSet<>());
    }
}
