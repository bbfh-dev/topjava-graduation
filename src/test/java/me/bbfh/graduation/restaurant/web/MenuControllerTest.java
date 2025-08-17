package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.AbstractControllerTest;
import me.bbfh.graduation.common.util.JsonUtil;
import me.bbfh.graduation.restaurant.MenuUtil;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.MenuTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.bbfh.graduation.restaurant.MenuTestData.*;
import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANTS;
import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANT_1;
import static me.bbfh.graduation.user.UserTestData.ADMIN_MAIL;
import static me.bbfh.graduation.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @BeforeEach
    public void repopulate() {
        if (!restaurantRepository.existsById(RESTAURANT_1.id())) {
            restaurantRepository.deleteAll();
            List<Restaurant> restaurants = restaurantRepository.saveAll(RESTAURANTS);
            IntStream.range(0, restaurants.size()).forEach(i -> RESTAURANTS.get(i).setId(restaurants.get(i).getId()));
        }

        menuRepository.deleteAll();
        List<Menu> menus = menuRepository.saveAll(MENUS);
        IntStream.range(0, MENUS.size()).forEach(i -> {
            Menu menu = MENUS.get(i);
            menu.setId(menus.get(i).getId());
        });

        dishRepository.deleteAll();
        List<Dish> dishes = dishRepository.saveAll(DISHES);
        IntStream.range(0, DISHES.size()).forEach(j -> {
            Dish dish = DISHES.get(j);
            dish.setId(dishes.get(j).getId());
        });
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(ProfileMenuController.REST_URL))
                .andDo(print())
                .andExpect(status().isOk());

        List<MenuTo> createdList = MENU_TO_MATCHER.readListFromJson(action);

        Map<Integer, Menu> expectedMap = MENUS.stream()
                .collect(Collectors.toMap(Menu::getId, r -> r));

        for (MenuTo created : createdList) {
            Menu expected = expectedMap.get(created.getId());
            if (expected == null) {
                throw new IllegalStateException("There must be no extra restaurants");
            }
            MenuTo expectedTo = MenuUtil.getTo(expected, dishRepository);
            MENU_TO_MATCHER.assertMatch(created, expectedTo);
            MENU_TO_MATCHER.assertMatch(MenuUtil.getTo(menuRepository.getExisted(created.id()), dishRepository), expectedTo);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileMenuController.REST_URL + "/" + MENU_NOT_EXIST_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(ProfileMenuController.REST_URL + "/" + MENU_1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        MenuTo created = MENU_TO_MATCHER.readFromJson(action);
        MenuTo expected = MenuUtil.getTo(MENU_1, dishRepository);
        expected.setId(created.getId());

        MENU_TO_MATCHER.assertMatch(created, expected);
        MENU_TO_MATCHER.assertMatch(MenuUtil.getTo(menuRepository.getExisted(created.id()), dishRepository), expected);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        MenuTo newTo = getNewTo();
        Assertions.assertNotNull(newTo.getRestaurantId());
        Restaurant restaurantRef = restaurantRepository.getReferenceById(newTo.getRestaurantId());
        Menu newMenu = MenuUtil.getModel(newTo, restaurantRef);

        ResultActions action = perform(MockMvcRequestBuilders.post(AdminMenuController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        MenuTo createdTo = MENU_TO_MATCHER.readFromJson(action);
        Menu created = MenuUtil.getModel(createdTo, restaurantRef);
        int newId = created.id();
        newMenu.setId(newId);
        newTo.setId(newId);
        IntStream.range(0, newTo.getDishes().size()).forEach(i -> {
            MenuTo.DishTo dishTo = newTo.getDishes().get(i);
            dishTo.setId(createdTo.getDishes().get(i).getId());
        });

        MENU_TO_MATCHER.assertMatch(createdTo, newTo);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
    }
}
