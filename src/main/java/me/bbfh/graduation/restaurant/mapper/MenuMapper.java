package me.bbfh.graduation.restaurant.mapper;

import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.to.DishTo;
import me.bbfh.graduation.restaurant.to.MenuTo;

import java.util.List;

public class MenuMapper {
    public static MenuTo toTo(Menu menu, List<DishTo> dishTos) {
        return new MenuTo(menu.getId(), menu.getRelevancyDate(), menu.getRestaurant().getId(), dishTos);
    }

    public static Menu toEntity(MenuTo menuTo, Restaurant restaurant) {
        return new Menu(menuTo.getId(), menuTo.getRelevancyDate(), restaurant);
    }
}

