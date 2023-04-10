package ua.epam.mishchenko.ticketbooking.service.mongo.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoTicket;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUserAccount;
import ua.epam.mishchenko.ticketbooking.repository.mongo.MongoEventRepository;
import ua.epam.mishchenko.ticketbooking.repository.mongo.MongoTicketRepository;
import ua.epam.mishchenko.ticketbooking.repository.mongo.MongoUserRepository;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoTicketService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MongoTicketServiceImpl implements MongoTicketService {

    private static final Logger log = LoggerFactory.getLogger(MongoTicketServiceImpl.class);

    private final MongoUserRepository userRepository;

    private final MongoEventRepository eventRepository;

    private final MongoTicketRepository ticketRepository;

    private final MongoTemplate template;

    @Override
    public MongoTicket bookTicket(String userId, String eventId, int place, Category category) {
        log.info("Start booking a ticket for user with id {}, event with id event {}, place {}, category {}",
                userId, eventId, place, category);
        try {
            return processBookingTicket(userId, eventId, place, category);
        } catch (RuntimeException e) {
            log.warn("Can not to book a ticket for user with id {}, event with id {}, place {}, category {}",
                    userId, eventId, place, category, e);
            return null;
        }
    }

    private MongoTicket processBookingTicket(String userId, String eventId, int place, Category category) {
        throwRuntimeExceptionIfUserNotExist(userId);
        throwRuntimeExceptionIfEventNotExist(eventId);
        throwRuntimeExceptionIfTicketAlreadyBooked(eventId, place, category);
        MongoUserAccount userAccount = userRepository.findById(userId).get().getUserAccount();
        MongoEvent event = getEvent(eventId);
        throwRuntimeExceptionIfUserNotHaveEnoughMoney(userAccount, event);
        buyTicket(userAccount, event);
        MongoTicket ticket = saveBookedTicket(userId, eventId, place, category);
        log.info("Successfully booking of the ticket: {}", ticket);
        return ticket;
    }

    private MongoTicket saveBookedTicket(String userId, String eventId, int place, Category category) {
        return ticketRepository.save(createNewTicket(userId, eventId, place, category));
    }

    private void buyTicket(MongoUserAccount userAccount, MongoEvent event) {
        userAccount.setMoney(subtractTicketPriceFromUserMoney(userAccount, event));
    }

    private BigDecimal subtractTicketPriceFromUserMoney(MongoUserAccount userAccount, MongoEvent event) {
        return userAccount.getMoney().subtract(event.getTicketPrice());
    }

    private void throwRuntimeExceptionIfUserNotHaveEnoughMoney(MongoUserAccount userAccount, MongoEvent event) {
        if (!userHasEnoughMoneyForTicket(userAccount, event)) {
            throw new RuntimeException(
                    "The user does not have enough money for ticket with event id " + event.getId()
            );
        }
    }

    private void throwRuntimeExceptionIfTicketAlreadyBooked(String eventId, int place, Category category) {
        MongoEvent event = eventRepository.findById(eventId).orElse(null);
        Query query = new Query();
        query.addCriteria(Criteria.where("place").is(place))
                .addCriteria(Criteria.where("category").is(category))
                .addCriteria(Criteria.where("event").is(event));
        MongoTicket ticket = template.findOne(query, MongoTicket.class);
        if (!Objects.isNull(ticket)) {
            throw new RuntimeException("This ticket already booked");
        }
    }

    private MongoEvent getEvent(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Can not to find an event by id: " + eventId));
    }

    private void throwRuntimeExceptionIfEventNotExist(String eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("The event with id " + eventId + " does not exist");
        }
    }

    private void throwRuntimeExceptionIfUserNotExist(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("The user with id " + userId + " does not exist");
        }
    }

    private boolean userHasEnoughMoneyForTicket(MongoUserAccount userAccount, MongoEvent event) {
        return userAccount.getMoney().compareTo(event.getTicketPrice()) > -1;
    }

    private MongoTicket createNewTicket(String userId, String eventId, int place, Category category) {
        MongoUser user = userRepository.findById(userId).get();
        MongoEvent event = eventRepository.findById(eventId).get();
        return MongoTicket.builder()
                .event(event)
                .user(user)
                .place(place)
                .category(category)
                .build();
    }

    @Override
    public List<MongoTicket> getBookedTickets(MongoUser user) {
        log.info("Finding all booked tickets by user {} ", user);
        if (Objects.isNull(user)) {
            log.warn("The user can not be a null");
            return new ArrayList<>();
        }
        MongoUser foundUser = userRepository.findById(user.getId()).get();
        Query query = new Query();
        query.addCriteria(Criteria.where("user").is(foundUser));

        return template.find(query, MongoTicket.class);
    }

    @Override
    public List<MongoTicket> getBookedTickets(MongoEvent event) {
        log.info("Finding all booked tickets by event {} ", event);

        if (Objects.isNull(event)) {
            log.warn("The event can not be a null");
            return new ArrayList<>();
        }
        MongoEvent foundEvent = eventRepository.findById(event.getId()).get();
        Query query = new Query();
        query.addCriteria(Criteria.where("event").is(foundEvent));

        return template.find(query, MongoTicket.class);
    }

    @Override
    public boolean cancelTicket(String ticketId) {
        log.info("Start canceling a ticket with id: {}", ticketId);
        ticketRepository.deleteById(ticketId);
        return !ticketRepository.existsById(ticketId);
    }

    @Override
    public boolean saveAll(List<MongoTicket> tickets) {
        log.info("Start saving a collection of the tickets");
        if (!Objects.isNull(tickets) && !tickets.isEmpty()) {
            List<MongoTicket> saved = ticketRepository.saveAll(tickets);
            return !Objects.isNull(saved) && tickets.size() == saved.size();
        } else {
            log.warn("Can not find a list of tickets to save");
            return true;
        }
    }
}
