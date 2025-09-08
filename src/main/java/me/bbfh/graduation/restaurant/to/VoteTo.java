package me.bbfh.graduation.restaurant.to;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.bbfh.graduation.common.HasId;
import me.bbfh.graduation.common.to.BaseTo;
import me.bbfh.graduation.restaurant.model.Vote;

import java.time.LocalDate;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements HasId {

    @NotNull
    Integer menuId;

    @NotNull
    LocalDate voteDate;

    public VoteTo(Integer id, Integer menuId, LocalDate voteDate) {
        super(id);
        this.menuId = menuId;
        this.voteDate = voteDate;
    }

    @Value
    @NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class RestTo {
        @NotNull
        Integer restaurantId;
    }
}
