package ua.epam.mishchenko.ticketbooking.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoTicket;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoEventService;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoTicketService;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/tickets")
public class MongoTicketsController {
    private static final Logger log = LoggerFactory.getLogger(MongoTicketsController.class);

    private final MongoEventService service;
    private final MongoUserService userService;
    private final MongoTicketService ticketService;

    @PostMapping
    public ModelAndView bookTicket(@RequestParam String userId,
                                   @RequestParam String eventId,
                                   @RequestParam int place,
                                   @RequestParam Category category) {
        log.info("Booking a ticket: userId={}, eventId={}, place={}, category={}", userId, eventId, place, category);
        Map<String, Object> model = new HashMap<>();
        MongoTicket ticket = ticketService.bookTicket(userId, eventId, place, category);
        if (Objects.isNull(ticket)) {
            model.put("message", "Can not book a ticket");
        } else {
            model.put("ticket", ticket);
        }
        return new ModelAndView("ticket", model);
    }

    @GetMapping("/user/{userId}")
    public ModelAndView showTicketsByUser(@PathVariable String userId) {
        log.info("Showing the tickets by user with id: {}", userId);
        Map<String, Object> model = new HashMap<>();
        MongoUser userById = userService.getUserById(userId);
        if (isNull(userById)) {
            model.put("message", "Can not find tickets as the User with id: '" + userId + "' is not found.");
        } else {
            List<MongoTicket> bookedTickets = ticketService.getBookedTickets(userById);
            if (bookedTickets.isEmpty()) {
                model.put("message", "Can not find the tickets for the User with id: " + userId);
            } else {
                model.put("tickets", bookedTickets);
            }
        }
        return new ModelAndView("tickets", model);
    }

    @GetMapping("/event/{eventId}")
    public ModelAndView showTicketsByEvent(@PathVariable String eventId) {
        log.info("Showing the tickets by event with id: {}", eventId);
        Map<String, Object> model = new HashMap<>();
        MongoEvent eventById = service.getEventById(eventId);
        if (isNull(eventById)) {
            model.put("message", "Can not find tickets as the Event with id: '" + eventId + "' is not found");
        } else {
            List<MongoTicket> bookedTickets = ticketService.getBookedTickets(eventById);
            if (bookedTickets.isEmpty()) {
                model.put("message", "Can not to find the tickets by event with id: " + eventId);
            } else {
                model.put("tickets", bookedTickets);
            }
        }
        return new ModelAndView("tickets", model);
    }

    @DeleteMapping("/{id}")
    public ModelAndView cancelTicket(@PathVariable String id) {
        log.info("Canceling ticket with id: {}", id);
        Map<String, Object> model = new HashMap<>();
        boolean canceled = ticketService.cancelTicket(id);
        if (canceled) {
            model.put("message", "The ticket with id: " + id + " successfully canceled");
        } else {
            model.put("message", "The ticket with id: " + id + " not canceled");
        }
        return new ModelAndView("ticket", model);
    }
}
