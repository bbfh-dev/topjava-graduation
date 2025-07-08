package me.bbfh.graduation.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.bbfh.graduation.common.model.BaseEntity;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends BaseEntity {
    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(max = 128)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;
}
