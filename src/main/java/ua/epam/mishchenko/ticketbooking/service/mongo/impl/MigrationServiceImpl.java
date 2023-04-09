package ua.epam.mishchenko.ticketbooking.service.mongo.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoTicket;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUserAccount;
import ua.epam.mishchenko.ticketbooking.service.mongo.MigrationService;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoEventService;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoTicketService;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MigrationServiceImpl implements MigrationService {

    private final UserService userService;
    private final MongoUserService mongoUserService;

    private final EventService eventService;
    private final MongoEventService mongoEventService;

    private final TicketService ticketService;
    private final MongoTicketService mongoTicketService;

    public boolean migrateToMongoDB() {
        boolean migrationSussess = true;
        migrationSussess = migrationSussess && migrateUsers();
        if (migrationSussess) {
            migrationSussess = migrationSussess && migrateEvents();
        }
        if (migrationSussess) {
            migrationSussess = migrationSussess && migrateTickets();
        }
        return migrationSussess;
    }

    private boolean migrateTickets() {
        final List<Ticket> tickets = ticketService.getAllTickets();
        List<MongoTicket> mongoTickets = convertToMongoTickets(tickets);
        return mongoTicketService.saveAll(mongoTickets);
    }

    private boolean migrateEvents() {
        final List<Event> events = eventService.getAllEvents();
        List<MongoEvent> mongoEvents = convertToMongoEvents(events);
        return mongoEventService.saveAll(mongoEvents);
    }

    private boolean migrateUsers() {
        final List<User> users = userService.getAllUsers();
        List<MongoUser> mongoUsers = convertToMongoUsers(users);
        return mongoUserService.saveAll(mongoUsers);
    }

    private List<MongoUser> convertToMongoUsers(List<User> users) {
        return users.stream()
                .map(u -> MongoUser.builder()
                        .userId(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .userAccount(MongoUserAccount.builder()
                                .money(u.getUserAccount().getMoney())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MongoTicket> convertToMongoTickets(List<Ticket> tickets) {
        return tickets.stream()
                .map(t -> MongoTicket.builder()
                        .place(t.getPlace())
                        .category(t.getCategory())
                        .user(mongoUserService.getUserByUserId(t.getUser().getId()))
                        .event(mongoEventService.getEventByEventId(t.getEvent().getId()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<MongoEvent> convertToMongoEvents(List<Event> events) {
        return events.stream()
                .map(ev -> MongoEvent.builder()
                        .eventId(ev.getId())
                        .title(ev.getTitle())
                        .date(ev.getDate())
                        .ticketPrice(ev.getTicketPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
