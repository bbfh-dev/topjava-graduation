package me.bbfh.graduation.restaurant.repository;

import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.user.model.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
