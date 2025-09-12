package me.bbfh.graduation.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.app.AuthUser;
import me.bbfh.graduation.restaurant.mapper.RestaurantMapper;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.RestaurantTo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    protected final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Cacheable(value = "restaurants")
    public List<RestaurantTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll {}", authUser);
        return RestaurantMapper.toTos(repository.findAll());
    }

    @GetMapping("/{restaurantId}")
    @Cacheable(value = "restaurants", key = "#restaurantId", unless = "#result == null")
    public RestaurantTo get(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get id={} by {}", restaurantId, authUser);
        return RestaurantMapper.toTo(repository.getExisted(restaurantId));
    }
}
