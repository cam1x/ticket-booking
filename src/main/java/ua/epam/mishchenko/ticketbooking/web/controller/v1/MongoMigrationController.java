package ua.epam.mishchenko.ticketbooking.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.epam.mishchenko.ticketbooking.facade.impl.BookingFacadeImpl;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/migrate")
public class MongoMigrationController {

    private static final Logger log = LoggerFactory.getLogger(MongoMigrationController.class);

    private final BookingFacadeImpl bookingFacade;

    /**
     * Migrate to MongoDB
     *
     * @return the model and view
     */
    @GetMapping
    public ModelAndView migrate() {
        log.info("Migration to MongoDB");
        Map<String, Object> model = new HashMap<>();
        boolean success = bookingFacade.migrateToMongoDB();
        if (success) {
            model.put("message", "Migration to MongoDB was successful");
            log.info("Migration to MongoDB was successful");
        } else {
            model.put("message", "Migration to MongoDB failed");
            log.info("Migration to MongoDB failed");
        }
        return new ModelAndView("user", model);
    }
}
