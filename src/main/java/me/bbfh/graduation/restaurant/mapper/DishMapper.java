package me.bbfh.graduation.restaurant.mapper;

import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.to.DishTo;

import java.util.Set;
import java.util.stream.Collectors;

public class DishMapper {
    public static DishTo toTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    public static Dish toEntity(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
    }

    public static Set<Dish> toEntities(Set<DishTo> dishTos) {
        return dishTos.stream().map(DishMapper::toEntity).collect(Collectors.toSet());
    }
}
