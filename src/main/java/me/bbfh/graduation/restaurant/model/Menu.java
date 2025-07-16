package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.bbfh.graduation.common.model.BaseEntity;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "relevancy_date"}, name = "menu_unique_restaurant_date_idx")})
public class Menu extends BaseEntity {

    @Column(name = "relevancy_date", nullable = false, columnDefinition = "timestamp")
    @NotNull
    private Date relevancyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;
}
