package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.AbstractControllerTest;
import me.bbfh.graduation.common.util.JsonUtil;
import me.bbfh.graduation.restaurant.RestaurantUtil;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.to.RestaurantTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.bbfh.graduation.restaurant.RestaurantTestData.*;
import static me.bbfh.graduation.user.UserTestData.ADMIN_MAIL;
import static me.bbfh.graduation.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest extends AbstractControllerTest {

    private static final String ADMIN_REST_URL_SLASH = AdminRestaurantController.REST_URL + '/';
    private static final String PROFILE_REST_URL_SLASH = ProfileRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @BeforeEach
    public void repopulate() {
        repository.deleteAll();
        List<Restaurant> restaurants = repository.saveAll(RESTAURANTS);
        IntStream.range(0, restaurants.size()).forEach(i -> RESTAURANTS.get(i).setId(restaurants.get(i).getId()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(PROFILE_REST_URL_SLASH + RESTAURANT_1.id()))
                .andDo(print())
                .andExpect(status().isOk());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        RESTAURANT_MATCHER.assertMatch(created, RESTAURANT_1);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(created.id()), RESTAURANT_1);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_REST_URL_SLASH + RESTAURANT_ID_UNKNOWN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(ProfileRestaurantController.REST_URL))
                .andDo(print())
                .andExpect(status().isOk());

        List<Restaurant> createdList = RESTAURANT_MATCHER.readListFromJson(action);

        Map<Integer, Restaurant> expectedMap = RESTAURANTS.stream()
                .collect(Collectors.toMap(Restaurant::getId, r -> r));

        for (Restaurant created : createdList) {
            Restaurant expected = expectedMap.get(created.getId());
            if (expected == null) {
                throw new IllegalStateException("There must be no extra restaurants");
            }
            RESTAURANT_MATCHER.assertMatch(created, expected);
            RESTAURANT_MATCHER.assertMatch(repository.getExisted(created.id()), expected);
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void noPermission() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL_SLASH + RESTAURANT_1.id()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        RestaurantTo newTo = new RestaurantTo(null, RESTAURANT_NEW.getName());
        Restaurant newRestaurant = RestaurantUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant restaurant = RESTAURANT_1;
        RestaurantTo newTo = new RestaurantTo(restaurant.getId(), restaurant.getName());
        ResultActions action = perform(MockMvcRequestBuilders
                .put(ADMIN_REST_URL_SLASH + restaurant.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isOk());

        Restaurant updated = RESTAURANT_MATCHER.readFromJson(action);
        RESTAURANT_MATCHER.assertMatch(updated, restaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(updated.id()), restaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_REST_URL_SLASH + RESTAURANT_1.id()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        Assert.isTrue(!repository.existsById(RESTAURANT_1.id()), "restaurant must be deleted");
    }
}
