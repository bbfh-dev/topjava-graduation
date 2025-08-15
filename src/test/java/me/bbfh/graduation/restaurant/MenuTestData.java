package me.bbfh.graduation.restaurant;

import me.bbfh.graduation.MatcherFactory;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.to.MenuTo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANTS;

public class MenuTestData {
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class);
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class);
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class);
    public static final MatcherFactory.Matcher<MenuTo.DishTo> DISH_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.DishTo.class);

    private static final int MENU1_ID = 1;

    public static final int MENU_NOT_EXIST_ID = MENU1_ID + 100_000;

    public static final Menu MENU_1 = new Menu(MENU1_ID, LocalDate.of(2024, 1, 12), RestaurantTestData.RESTAURANT_1);
    public static final Menu MENU_2 = new Menu(MENU1_ID + 1, LocalDate.of(2024, 1, 13), RestaurantTestData.RESTAURANT_1);

    public static List<Menu> MENUS = List.of(MENU_1, MENU_2);

    private static final int DISH1_ID = 1;

    public static final Dish DISH_1 = new Dish(DISH1_ID, "Example Dish #1 of Menu #1", BigDecimal.valueOf(10.5), MENU_1);
    public static final Dish DISH_2 = new Dish(DISH1_ID + 1, "Example Dish #2 of Menu #1", BigDecimal.valueOf(6.0), MENU_1);
    public static final Dish DISH_3 = new Dish(DISH1_ID + 2, "Example Dish #1 of Menu #2", BigDecimal.valueOf(16.0), MENU_2);

    public static List<Dish> DISHES = List.of(DISH_1, DISH_2, DISH_3);

    public static MenuTo getNewTo() {
        return new MenuTo(null, LocalDate.now(), RESTAURANTS.getFirst().id(), List.of(
                new MenuTo.DishTo(null, "Example Dish #A", BigDecimal.valueOf(12.0f)),
                new MenuTo.DishTo(null, "Example Dish #B", BigDecimal.valueOf(50.0f))
        ));
    }
}
