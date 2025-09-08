package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.AbstractControllerTest;
import me.bbfh.graduation.GraduationApplication;
import me.bbfh.graduation.common.BaseRepository;
import me.bbfh.graduation.common.model.BaseEntity;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import me.bbfh.graduation.restaurant.repository.VoteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.IntStream;

import static me.bbfh.graduation.restaurant.MenuTestData.*;
import static me.bbfh.graduation.restaurant.RestaurantTestData.RESTAURANTS;
import static me.bbfh.graduation.restaurant.VoteTestData.VOTES;

@Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {

    @BeforeAll
    public static void disablePrepopulation() {
        GraduationApplication.doPopulateWithDemoData = false;
    }
}
