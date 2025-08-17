package me.bbfh.graduation.restaurant.to;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import me.bbfh.graduation.common.HasId;
import me.bbfh.graduation.common.to.BaseTo;
import me.bbfh.graduation.common.to.NamedTo;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;

import java.math.BigDecimal;
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

    public MenuTo(Menu menu, @NotNull List<DishTo> dishes) {
        super(menu.getId());
        this.relevancyDate = menu.getRelevancyDate();
        this.restaurantId = menu.getRestaurant().getId();
        this.dishes = dishes;
    }

    public static class DishTo extends NamedTo {

        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        @Digits(integer = 5, fraction = 2)
        BigDecimal price;

        public DishTo(Integer id, String name, BigDecimal price) {
            super(id, name);
            this.price = price;
        }

        public DishTo(Dish dish) {
            super(dish.getId(), dish.getName());
            this.price = dish.getPrice();
        }

        public Dish toModel(Menu menu) {
            return new Dish(id, name, price, menu);
        }
    }
}
