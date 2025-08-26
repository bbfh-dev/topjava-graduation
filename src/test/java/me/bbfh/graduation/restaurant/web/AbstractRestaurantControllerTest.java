package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.AbstractControllerTest;
import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.common.model.BaseEntity;
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
import static me.bbfh.graduation.restaurant.VoteTestData.VOTES;

public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private VoteRepository voteRepository;

    private <T extends BaseEntity> void repopulateModelsIfMissing(List<T> items, BaseRepository<T> repository) {
        if (items.stream()
                .allMatch(i -> repository.existsById(i.getId()))) {
            return;
        }

        repository.deleteAll();
        List<T> dbItems = repository.saveAll(items);
        IntStream.range(0, items.size()).forEach(i -> {
            T item = items.get(i);
            item.setId(dbItems.get(i).getId());
        });
    }

    @BeforeEach
    public void repopulate() {
        // WARN: Unoptimized. Iterates over every single element before each test.
        // But is kept just to ensure that tests don't fail for wrong reasons.
        // Can be replaced with a proper integrity check.
        repopulateModelsIfMissing(RESTAURANTS, restaurantRepository);
        repopulateModelsIfMissing(MENUS, menuRepository);
        repopulateModelsIfMissing(DISHES, dishRepository);
        repopulateModelsIfMissing(VOTES, voteRepository);
    }
}
