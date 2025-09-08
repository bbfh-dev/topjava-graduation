package me.bbfh.graduation.restaurant.web;

import me.bbfh.graduation.AbstractControllerTest;
import me.bbfh.graduation.GraduationApplication;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {

    @BeforeAll
    public static void disablePrepopulation() {
        GraduationApplication.doPopulateWithDemoData = false;
    }
}
