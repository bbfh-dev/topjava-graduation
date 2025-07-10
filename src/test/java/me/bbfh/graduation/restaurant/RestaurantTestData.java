package me.bbfh.graduation.restaurant;

import me.bbfh.graduation.MatcherFactory;
import me.bbfh.graduation.restaurant.model.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    private static final int RESTAURANT_ID_1 = 1;
    private static final int RESTAURANT_ID_2 = 2;
    private static final String RESTAURANT_NAME_1 = "Example Restaurant #1";
    private static final String RESTAURANT_NAME_2 = "Example Restaurant #2";

    public static List<Restaurant> RESTAURANTS = List.of(
            new Restaurant(RESTAURANT_ID_1, RESTAURANT_NAME_1),
            new Restaurant(RESTAURANT_ID_2, RESTAURANT_NAME_2)
    );

    public static final int RESTAURANT_ID_UNKNOWN = 1000;
    public static final String RESTAURANT_NAME_3 = "Example Restaurant #3";
}
