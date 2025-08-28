package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.bbfh.graduation.common.model.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "relevancy_date"}, name = "menu_unique_restaurant_date_idx")})
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "relevancy_date", nullable = false, columnDefinition = "date")
    private LocalDate relevancyDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Menu(Integer id, LocalDate relevancyDate, Restaurant restaurant) {
        super(id);
        this.relevancyDate = relevancyDate;
        this.restaurant = restaurant;
    }
}
