package me.bbfh.graduation.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.app.AuthUser;
import me.bbfh.graduation.common.error.NotFoundException;
import me.bbfh.graduation.restaurant.MenuUtil;
import me.bbfh.graduation.restaurant.mapper.DishMapper;
import me.bbfh.graduation.restaurant.mapper.MenuMapper;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import static me.bbfh.graduation.common.validation.ValidationUtil.assureIdConsistent;
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
    @Transactional
    public ResponseEntity<MenuTo> create(@Valid @RequestBody MenuTo menuTo) {
        log.info("create {}", menuTo);
        checkNew(menuTo);

        Assert.notNull(menuTo.getRestaurantId(), "restaurant id must be defined");
        Menu menu = menuRepository.save(MenuMapper.toEntity(menuTo, restaurantRepository.getReferenceById(menuTo.getRestaurantId())));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource)
                .body(MenuUtil.getToFetchDishes(menu));
    }

    @PutMapping(value = "/{menuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public MenuTo update(@PathVariable int menuId, @Valid @RequestBody MenuTo menuTo) {
        log.info("update {}", menuTo);
        assureIdConsistent(menuTo, menuId);

        Menu originalMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found with id=" + menuId));
        Set<Dish> originalDishes = originalMenu.getDishes().stream()
                .map(Dish::new)
                .collect(Collectors.toSet());

        Assert.notNull(menuTo.getRestaurantId(), "restaurant id must be defined");
        Menu updatedMenu = menuRepository.save(MenuMapper.toEntity(menuTo,
                restaurantRepository.getReferenceById(menuTo.getRestaurantId())));

        // Delete orphans.
        // Because a new Menu is created instead of modifying the [originalMenu] it isn't done automatically.
        originalDishes.forEach(originalDish -> {
            if (!updatedMenu.getDishes().contains(originalDish)) {
                dishRepository.deleteById(originalDish.getId());
            }
        });

        return MenuMapper.toTo(updatedMenu, updatedMenu.getDishes().stream()
                .map(DishMapper::toTo)
                .collect(Collectors.toSet()));
    }

    @DeleteMapping(value = "/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int menuId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("delete id={} by {}", menuId, authUser);
        menuRepository.deleteExisted(menuId);
    }
}
