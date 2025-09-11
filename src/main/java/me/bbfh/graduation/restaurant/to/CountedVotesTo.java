package me.bbfh.graduation.restaurant.to;

import jakarta.validation.constraints.NotNull;

public record CountedVotesTo(
        @NotNull Integer menuId,
        @NotNull Integer count
) {
}
