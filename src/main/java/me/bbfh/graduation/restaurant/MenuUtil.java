package me.bbfh.graduation.restaurant;

import lombok.experimental.UtilityClass;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;

import java.util.List;

@UtilityClass
public class MenuUtil {

    public static List<MenuTo> getTosWithDishes(List<Menu> menus, DishRepository repository) {
        return menus.stream()
                .map(menu -> getTo(menu, repository))
                .toList();
    }

    public static MenuTo getTo(Menu menu, DishRepository repository) {
        return new MenuTo(menu,
                repository.getAll(menu.getId()).stream()
                        .map(MenuTo.DishTo::new)
                        .toList()
        );
    }

    public static Menu getModel(MenuTo menuTo, Restaurant restaurant) {
        return new Menu(menuTo.getId(), menuTo.getRelevancyDate(), restaurant);
    }
}
