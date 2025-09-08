package me.bbfh.graduation.restaurant.repository;


import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT d FROM Menu d WHERE d.restaurant.id=:restaurantId AND d.relevancyDate=:date")
    Optional<Menu> getByRestaurantIdAndDate(int restaurantId, LocalDate date);

    @Query("SELECT d FROM Menu d WHERE d.relevancyDate=:date")
    List<Menu> getByDate(LocalDate date);
}
