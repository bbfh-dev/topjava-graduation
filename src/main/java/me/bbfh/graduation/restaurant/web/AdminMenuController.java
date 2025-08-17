package me.bbfh.graduation.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.restaurant.MenuUtil;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static me.bbfh.graduation.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class AdminMenuController {
    static final String REST_URL = "/api/admin/menus";

    protected final RestaurantRepository restaurantRepository;

    protected final MenuRepository menuRepository;

    protected final DishRepository dishRepository;

    public AdminMenuController(RestaurantRepository restaurantRepository, MenuRepository menuRepository,
                               DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<MenuTo> create(@Valid @RequestBody MenuTo menuTo) {
        log.info("create {}", menuTo);
        checkNew(menuTo);

        Menu menu = menuRepository.save(MenuUtil.getModel(menuTo, restaurantRepository.getReferenceById(menuTo.getRestaurantId())));
        List<Dish> dishes = menuTo.getDishes().stream()
                .map(dishTo -> dishRepository.save(dishTo.toModel(menu)))
                .toList();

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(new MenuTo(menu, dishes.stream()
                .map(MenuTo.DishTo::new)
                .toList()));
    }
}
