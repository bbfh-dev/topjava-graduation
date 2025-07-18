package me.bbfh.graduation.restaurant;

import me.bbfh.graduation.MatcherFactory;
import me.bbfh.graduation.restaurant.model.Restaurant;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final Restaurant RESTAURANT_1 = new Restaurant(1, "Example Restaurant #1");
    public static final Restaurant RESTAURANT_2 = new Restaurant(2, "Пример Ресторана №2");
    public static final Restaurant RESTAURANT_3 = new Restaurant(3, "Example Restaurant #3");
    public static final Restaurant RESTAURANT_NEW = new Restaurant(null, "New Restaurant");
    public static List<Restaurant> RESTAURANTS = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3);

    public static final int RESTAURANT_ID_UNKNOWN = Integer.MAX_VALUE;
}
