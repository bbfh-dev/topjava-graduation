package me.bbfh.graduation.restaurant.repository;


import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT d FROM #{#entityName} d WHERE d.restaurant.id=:restaurantId")
    List<Menu> getAll(int restaurantId);
}
