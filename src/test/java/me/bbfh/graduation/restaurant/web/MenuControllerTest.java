package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.common.util.JsonUtil;
import me.bbfh.graduation.restaurant.MenuUtil;
import me.bbfh.graduation.restaurant.mapper.MenuMapper;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.DishTo;
import me.bbfh.graduation.restaurant.to.MenuTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.IntStream;

import static me.bbfh.graduation.restaurant.MenuTestData.*;
import static me.bbfh.graduation.user.UserTestData.ADMIN_MAIL;
import static me.bbfh.graduation.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends AbstractRestaurantControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(MenuController.REST_URL + "/" + MENU_NOT_EXIST_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(MenuController.REST_URL + "/" + MENUS.getFirst().getId()))
                .andDo(print())
                .andExpect(status().isOk());

        MenuTo created = MENU_TO_MATCHER.readFromJson(action);
        MenuTo expected = MenuUtil.getToFetchDishes(MENUS.getFirst(), dishRepository);
        expected.setId(created.getId());

        MENU_TO_MATCHER.assertMatch(created, expected);
        MENU_TO_MATCHER.assertMatch(MenuUtil.getToFetchDishes(menuRepository.getExisted(created.id()), dishRepository), expected);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        MenuTo newTo = getNewTo();
        Assertions.assertNotNull(newTo.getRestaurantId());
        Restaurant restaurantRef = restaurantRepository.getReferenceById(newTo.getRestaurantId());
        Menu newMenu = MenuMapper.toEntity(newTo, restaurantRef);

        ResultActions action = perform(MockMvcRequestBuilders.post(AdminMenuController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        MenuTo createdTo = MENU_TO_MATCHER.readFromJson(action);
        Menu created = MenuMapper.toEntity(createdTo, restaurantRef);
        int newId = created.id();
        newMenu.setId(newId);
        newTo.setId(newId);
        Assertions.assertNotNull(newTo.getDishes());
        IntStream.range(0, newTo.getDishes().size()).forEach(i -> {
            DishTo dishTo = newTo.getDishes().get(i);
            Assertions.assertNotNull(createdTo.getDishes());
            dishTo.setId(createdTo.getDishes().get(i).getId());
        });

        MENU_TO_MATCHER.assertMatch(createdTo, newTo);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo updatedTo = getUpdatedTo();
        Assertions.assertNotNull(updatedTo.getRestaurantId());
        Restaurant restaurantRef = restaurantRepository.getReferenceById(updatedTo.getRestaurantId());
        Menu updatedMenu = MenuMapper.toEntity(updatedTo, restaurantRef);

        ResultActions action = perform(MockMvcRequestBuilders.put(AdminMenuController.REST_URL + "/" + MENUS.getFirst().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isOk());

        MenuTo responseMenuTo = MENU_TO_MATCHER.readFromJson(action);
        Menu responseMenu = MenuMapper.toEntity(responseMenuTo, restaurantRef);
        int newId = responseMenu.id();
        updatedMenu.setId(newId);
        updatedTo.setId(newId);
        Assertions.assertNotNull(updatedTo.getDishes());
        IntStream.range(0, updatedTo.getDishes().size()).forEach(i -> {
            DishTo dishTo = updatedTo.getDishes().get(i);
            Assertions.assertNotNull(responseMenuTo.getDishes());
            dishTo.setId(responseMenuTo.getDishes().get(i).getId());
        });

        MENU_TO_MATCHER.assertMatch(responseMenuTo, updatedTo);
        MENU_MATCHER.assertMatch(responseMenu, updatedMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), updatedMenu);
        Assertions.assertNull(dishRepository.findById(DISHES.get(1).getId()).orElse(null));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotExist() throws Exception {
        MenuTo updatedTo = getUpdatedTo();
        updatedTo.setId(MENU_NOT_EXIST_ID);
        perform(MockMvcRequestBuilders.put(AdminMenuController.REST_URL + "/" + MENU_NOT_EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(AdminMenuController.REST_URL + "/" + MENUS.get(1).getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertFalse(menuRepository.existsById(MENUS.get(1).getId()));
        Assertions.assertFalse(dishRepository.existsById(DISHES.get(2).getId()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotExist() throws Exception {
        perform(MockMvcRequestBuilders.delete(AdminMenuController.REST_URL + "/" + MENU_NOT_EXIST_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
