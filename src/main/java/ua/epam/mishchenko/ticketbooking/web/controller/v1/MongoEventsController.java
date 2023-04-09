package ua.epam.mishchenko.ticketbooking.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoEventService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ua.epam.mishchenko.ticketbooking.utils.Constants.DATE_FORMATTER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class MongoEventsController {
    private static final Logger log = LoggerFactory.getLogger(MongoEventsController.class);

    private final MongoEventService service;

    @GetMapping("/{id}")
    public ModelAndView showEventById(@PathVariable String id) {
        log.info("Showing event by id: {}", id);
        MongoEvent eventById = service.getEventById(id);
        Map<String, Object> model = new HashMap<>();
        if (Objects.isNull(eventById)) {
            model.put("message", "Can't get the Event by id: " + id);
        } else {
            model.put("event", eventById);
        }
        return new ModelAndView("event", model);
    }

    @GetMapping("/title/{title}")
    public ModelAndView showEventsByTitle(@PathVariable String title) {
        log.info("Showing events by title: {}", title);
        Map<String, Object> model = new HashMap<>();
        List<MongoEvent> eventsByTitle = service.getEventsByTitle(title);
        if (eventsByTitle.isEmpty()) {
            model.put("message", "Can not to get events by title: " + title);
        } else {
            model.put("events", eventsByTitle);
        }
        return new ModelAndView("events", model);
    }

    @GetMapping("/day/{day}")
    public ModelAndView showEventsForDay(@PathVariable String day) {
        log.info("Showing events for day: {}", day);
        Map<String, Object> model = new HashMap<>();
        try {
            Date date = parseFromStringToDate(day);
            List<MongoEvent> eventsForDay = service.getEventsForDay(date);
            if (eventsForDay.isEmpty()) {
                model.put("message", "Can not get Events for day: " + day);
            } else {
                model.put("events", eventsForDay);
            }
        } catch (RuntimeException e) {
            model.put("message", "Can not parse string '" + day + "' to Date object");
        }
        return new ModelAndView("events", model);
    }

    @PostMapping
    public ModelAndView createEvent(@RequestParam String title,
                                    @RequestParam String day,
                                    @RequestParam BigDecimal price) {
        MongoEvent event = createEventEntity(title, day, price);
        log.info("Creating an event " + event);
        Map<String, Object> model = new HashMap<>();
        try {
            MongoEvent createdEvent = service.createEvent(event);
            if (Objects.isNull(createdEvent)) {
                model.put("message", "Can not create an Event");
            } else {
                model.put("event", createdEvent);
            }
        } catch (RuntimeException e) {
            model.put("message", "Can not parse string '" + day + "' to date object");
        }
        return new ModelAndView("event", model);
    }

    @PutMapping
    public ModelAndView updateEvent(@RequestParam String id,
                                    @RequestParam String title,
                                    @RequestParam String day,
                                    @RequestParam BigDecimal price) {
        MongoEvent event = createEventEntity(id, title, day, price);
        log.info("Updating an event: " + event);
        Map<String, Object> model = new HashMap<>();
        try {
            MongoEvent updatedEvent = service.updateEvent(event);
            if (Objects.isNull(updatedEvent)) {
                model.put("message", "Can not update the Event with id: " + id);
            } else {
                model.put("event", updatedEvent);
            }
        } catch (RuntimeException e) {
            model.put("message", "Can not parse string '" + day + "' to Date object");
        }
        return new ModelAndView("event", model);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteEvent(@PathVariable String id) {
        log.info("Deleting an event with id: {}", id);
        Map<String, Object> model = new HashMap<>();
        boolean isEventDeleted = service.deleteEvent(id);
        if (isEventDeleted) {
            model.put("message", "The Event with id " + id + " is successfully deleted");
        } else {
            model.put("message", "The Event with id " + id + " is not deleted");
        }
        return new ModelAndView("event", model);
    }

    private MongoEvent createEventEntity(String title, String day, BigDecimal price) {
        return MongoEvent.builder()
                .title(title)
                .date(parseFromStringToDate(day))
                .ticketPrice(price)
                .build();
    }

    private MongoEvent createEventEntity(String id, String title, String day, BigDecimal price) {
        return MongoEvent.builder()
                .id(id)
                .title(title)
                .date(parseFromStringToDate(day))
                .ticketPrice(price)
                .build();
    }

    private Date parseFromStringToDate(String date) {
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            log.warn("Can not to parse string {} to date object", date);
            throw new RuntimeException("Can not to parse string " + date + " to date object", e);
        }
    }

}
