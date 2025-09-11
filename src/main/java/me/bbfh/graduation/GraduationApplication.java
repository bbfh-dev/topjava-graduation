package me.bbfh.graduation;

import me.bbfh.graduation.common.util.DateTimeUtil;
import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class GraduationApplication {

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    public static boolean doPopulateWithDemoData = true;

    public GraduationApplication(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(GraduationApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabase() {
        if (!doPopulateWithDemoData) {
            return;
        }

        Restaurant restaurantA = restaurantRepository.save(new Restaurant(null, "My Restaurant A"));
        Restaurant restaurantB = restaurantRepository.save(new Restaurant(null, "My Restaurant B"));

        Set<Dish> dishesA1 = new HashSet<>();
        dishesA1.add(new Dish(null, "My Dish B1", (long) 1099));
        menuRepository.save(new Menu(null, DateTimeUtil.getCurrentDate().minusDays(1), restaurantA, dishesA1));

        Set<Dish> dishesA2 = new HashSet<>();
        dishesA2.add(new Dish(null, "My Dish A1", (long) 1700));
        dishesA2.add(new Dish(null, "My Dish A2", (long) 2550));
        menuRepository.save(new Menu(null, DateTimeUtil.getCurrentDate(), restaurantA, dishesA2));

        menuRepository.save(new Menu(null, DateTimeUtil.getCurrentDate(), restaurantB));
    }
}
