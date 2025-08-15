package me.bbfh.graduation.restaurant;

import lombok.experimental.UtilityClass;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MenuUtil {
    public static List<MenuTo> getTosWithDishes(List<Menu> menus, DishRepository repository) {
        return menus.stream()
                .map(menu -> getTo(menu, repository))
                .collect(Collectors.toList());
    }

    public static MenuTo getTo(Menu menu, DishRepository repository) {
        return new MenuTo(menu,
                repository.getAll(menu.getId()).stream()
                        .map(MenuTo.DishTo::new)
                        .collect(Collectors.toList())
        );
    }
}
