package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.AbstractControllerTest;
import me.bbfh.graduation.common.model.BaseEntity;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.model.Vote;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static me.bbfh.graduation.restaurant.MenuTestData.*;
import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANTS;
import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANT_1;
import static me.bbfh.graduation.restaurant.VoteTestData.VOTES;
import static me.bbfh.graduation.restaurant.VoteTestData.VOTE_1;

public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    public void repopulate() {
        if (RESTAURANTS.stream().noneMatch(r -> restaurantRepository.existsById(r.id()))) {
            restaurantRepository.deleteAll();
            List<Restaurant> restaurants = restaurantRepository.saveAll(RESTAURANTS);
            IntStream.range(0, restaurants.size()).forEach(i -> RESTAURANTS.get(i).setId(restaurants.get(i).getId()));
        }

        if (MENUS.stream().noneMatch(m -> menuRepository.existsById(m.id()))) {
            menuRepository.deleteAll();
            List<Menu> menus = menuRepository.saveAll(MENUS);
            IntStream.range(0, MENUS.size()).forEach(i -> {
                Menu menu = MENUS.get(i);
                menu.setId(menus.get(i).getId());
            });
        }

        if (DISHES.stream().noneMatch(d -> dishRepository.existsById(d.id()))) {
            dishRepository.deleteAll();
            List<Dish> dishes = dishRepository.saveAll(DISHES);
            IntStream.range(0, DISHES.size()).forEach(j -> {
                Dish dish = DISHES.get(j);
                dish.setId(dishes.get(j).getId());
            });
        }

        if (VOTES.stream().noneMatch(v -> voteRepository.existsById(v.id()))) {
            voteRepository.deleteAll();
            List<Vote> votes = voteRepository.saveAll(VOTES);
            IntStream.range(0, VOTES.size()).forEach(j -> {
                Vote vote = VOTES.get(j);
                vote.setId(votes.get(j).getId());
            });
        }
    }
}
