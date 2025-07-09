package me.bbfh.graduation.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.bbfh.graduation.app.AuthUser;
import me.bbfh.graduation.restaurant.RestaurantUtil;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.RestaurantTo;
import me.bbfh.graduation.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static me.bbfh.graduation.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/admin/restaurant";

    protected final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll {}", authUser);
        return authUser.getUser();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Restaurant> create(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("create {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
