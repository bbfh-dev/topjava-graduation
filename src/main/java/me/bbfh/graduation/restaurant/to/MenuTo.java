package me.bbfh.graduation.restaurant.to;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import me.bbfh.graduation.common.HasId;
import me.bbfh.graduation.common.to.BaseTo;

import java.time.LocalDate;
import java.util.List;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo implements HasId {

    @NotNull
    LocalDate relevancyDate;

    @NotNull
    Integer restaurantId;

    @NotNull
    List<DishTo> dishes;

    public MenuTo(Integer id, LocalDate relevancyDate, Integer restaurantId, List<DishTo> dishes) {
        super(id);
        this.relevancyDate = relevancyDate;
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }
}
