package me.bbfh.graduation.restaurant;

import lombok.experimental.UtilityClass;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.to.RestaurantTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@UtilityClass
public class RestaurantUtil {

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }

    public static void assureIsNotNew(RestaurantTo restaurantTo) {
        if (restaurantTo.isNew()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "restaurant must not be new, use /create");
        }
    }
}
