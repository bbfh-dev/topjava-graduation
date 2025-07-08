package me.bbfh.graduation.restaurant.repository;


import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.restaurant.model.Menu;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
}
