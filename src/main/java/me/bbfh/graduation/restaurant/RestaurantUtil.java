package me.bbfh.graduation.restaurant;

import lombok.experimental.UtilityClass;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.to.RestaurantTo;

@UtilityClass
public class RestaurantUtil {
    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(null, restaurantTo.getName());
    }
}
