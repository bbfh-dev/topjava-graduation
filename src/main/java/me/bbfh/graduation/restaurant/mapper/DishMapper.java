package me.bbfh.graduation.restaurant.mapper;

import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.to.DishTo;

public class DishMapper {
    public static DishTo toTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    public static Dish toEntity(DishTo dishTo, Menu menu) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice(), menu);
    }
}
