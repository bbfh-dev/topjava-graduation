package me.bbfh.graduation.restaurant.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import me.bbfh.graduation.common.HasId;
import me.bbfh.graduation.common.to.NamedTo;
import me.bbfh.graduation.restaurant.model.Restaurant;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements HasId {

    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }
}
