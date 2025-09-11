package me.bbfh.graduation.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.common.util.DateTimeUtil;
import me.bbfh.graduation.restaurant.MenuUtil;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class MenuController {

    static final String REST_URL = "/api/menus";

    protected final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping("/{menuId}")
    @Cacheable(value = "menus", key = "#menuId", unless = "#result == null")
    public MenuTo get(@PathVariable int menuId) {
        log.info("get id={}", menuId);
        return MenuUtil.getToFetchDishes(menuRepository.getExisted(menuId));
    }

    @GetMapping("/today")
    public List<MenuTo> getToday() {
        return MenuUtil.getTosFetchDishes(menuRepository.getByDate(DateTimeUtil.getCurrentDate()));
    }
}
