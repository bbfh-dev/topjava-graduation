package me.bbfh.graduation.restaurant;

import lombok.experimental.UtilityClass;
import me.bbfh.graduation.restaurant.mapper.DishMapper;
import me.bbfh.graduation.restaurant.mapper.MenuMapper;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.to.MenuTo;

import java.util.List;

@UtilityClass
public class MenuUtil {

    public static List<MenuTo> getTosFetchDishes(List<Menu> menus) {
        return menus.stream()
                .map(MenuUtil::getToFetchDishes)
                .toList();
    }

    public static MenuTo getToFetchDishes(Menu menu) {
        return MenuMapper.toTo(menu, menu.getDishes().stream()
                .map(DishMapper::toTo)
                .toList());
    }
}
