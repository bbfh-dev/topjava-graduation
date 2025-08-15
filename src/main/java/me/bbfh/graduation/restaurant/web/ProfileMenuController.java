package me.bbfh.graduation.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.restaurant.MenuUtil;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ProfileMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class ProfileMenuController {
    static final String REST_URL = "/api/profile/menus";

    protected final MenuRepository menuRepository;

    protected final DishRepository dishRepository;

    public ProfileMenuController(MenuRepository menuRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<MenuTo> getAll() {
        return MenuUtil.getTosWithDishes(menuRepository.findAll(), dishRepository);
    }

    @GetMapping("/{menuId}")
    public MenuTo get(@PathVariable int menuId) {
        log.info("get id={}", menuId);
        return MenuUtil.getTo(menuRepository.getExisted(menuId), dishRepository);
    }
}
