package me.bbfh.graduation;

import me.bbfh.graduation.restaurant.model.Dish;
import me.bbfh.graduation.restaurant.model.Menu;
import me.bbfh.graduation.restaurant.model.Restaurant;
import me.bbfh.graduation.restaurant.repository.DishRepository;
import me.bbfh.graduation.restaurant.repository.MenuRepository;
import me.bbfh.graduation.restaurant.repository.RestaurantRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;

@SpringBootApplication
public class GraduationApplication {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    public GraduationApplication(RestaurantRepository restaurantRepository, MenuRepository menuRepository,
                                 DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(GraduationApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabase() {
        Restaurant restaurantA = restaurantRepository.save(new Restaurant(null, "My Restaurant A"));
        Restaurant restaurantB = restaurantRepository.save(new Restaurant(null, "My Restaurant B"));

        Menu menuA1 = menuRepository.save(new Menu(null, LocalDate.now().minusDays(1), restaurantA));
        Menu menuA2 = menuRepository.save(new Menu(null, LocalDate.now(), restaurantA));
        menuRepository.save(new Menu(null, LocalDate.now(), restaurantB));

        dishRepository.save(new Dish(null, "My Dish A1", (long) 1700, menuA2));
        dishRepository.save(new Dish(null, "My Dish A2", (long) 2550, menuA2));
        dishRepository.save(new Dish(null, "My Dish B1", (long) 1099, menuA1));
    }
}
