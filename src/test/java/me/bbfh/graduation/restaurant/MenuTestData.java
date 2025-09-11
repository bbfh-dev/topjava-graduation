package me.bbfh.graduation.restaurant;

import me.bbfh.graduation.MatcherFactory;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.to.DishTo;
import me.bbfh.graduation.restaurant.to.MenuTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANTS;

public class MenuTestData {
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class);
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class);

    private static final int DISH1_ID = 1;
    private static final Dish DISH_1 = new Dish(DISH1_ID, "Example Dish #1 of Menu #1", (long) 1050);
    private static final Dish DISH_2 = new Dish(DISH1_ID + 1, "Example Dish #2 of Menu #1", (long) 600);
    private static final Dish DISH_3 = new Dish(DISH1_ID + 2, "Example Dish #1 of Menu #2", (long) 1600);
    public static List<Dish> DISHES = List.of(DISH_1, DISH_2, DISH_3);

    private static final int MENU1_ID = 1;
    public static final int MENU_NOT_EXIST_ID = MENU1_ID + 100_000;
    private static final Menu MENU_1 = new Menu(MENU1_ID, LocalDate.now().minusDays(1), RestaurantTestData.RESTAURANT_1, Set.of(DISH_1, DISH_2));
    private static final Menu MENU_2 = new Menu(MENU1_ID + 1, LocalDate.now(), RestaurantTestData.RESTAURANT_1, Set.of(DISH_3));
    public static List<Menu> MENUS = List.of(MENU_1, MENU_2);

    public static MenuTo getNewTo() {
        return new MenuTo(null, LocalDate.now(), RESTAURANTS.get(1).id(), getNewDishes());
    }

    public static MenuTo getUpdatedTo() {
        return new MenuTo(
                MENU_1.getId(),
                MENU_1.getRelevancyDate(),
                MENU_1.getRestaurant().getId(),
                Set.of(
                        new DishTo(DISH_1.getId(), "Updated Dish #1", (long) 1050),
                        new DishTo(null, "New Dish", (long) 835)
                )
        );
    }

    public static Set<DishTo> getNewDishes() {
        return Set.of(
                new DishTo(null, "Example Dish #A", (long) 1200),
                new DishTo(null, "Example Dish #B", (long) 5000)
        );
    }
}
