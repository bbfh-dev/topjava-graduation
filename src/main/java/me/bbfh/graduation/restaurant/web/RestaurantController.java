package me.bbfh.graduation.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.app.AuthUser;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
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
    public List<Restaurant> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll {}", authUser);
        return repository.findAll();
    }

    @GetMapping("/{restaurantId}")
    public Restaurant get(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get id={} by {}", restaurantId, authUser);
        return repository.getExisted(restaurantId);
    }
}
