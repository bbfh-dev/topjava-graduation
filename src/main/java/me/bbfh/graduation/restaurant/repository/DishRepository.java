package me.bbfh.graduation.restaurant.repository;

import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT e FROM Dish e WHERE e.menu.id=:menuId")
    List<Dish> getAll(int menuId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish e WHERE e.menu.id=:menuId")
    void deleteAll(int menuId);
}
