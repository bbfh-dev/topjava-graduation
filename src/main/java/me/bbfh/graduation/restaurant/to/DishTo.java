package me.bbfh.graduation.restaurant.to;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import me.bbfh.graduation.common.HasId;
import me.bbfh.graduation.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo implements HasId {

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    Long price;

    public DishTo(Integer id, String name, Long price) {
        super(id, name);
        this.price = price;
    }
}
