package me.bbfh.graduation.restaurant.mapper;

import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.to.RestaurantTo;

public class RestaurantMapper {

    public static RestaurantTo toTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static Restaurant toEntity(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }
}
