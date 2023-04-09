package ua.epam.mishchenko.ticketbooking.service.mongo;

import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoTicket;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;

import java.util.List;

/**
 * The interface Ticket service.
 */
public interface MongoTicketService {

    /**
     * Book ticket MongoTicket.
     *
     * @param userId   the user id
     * @param eventId  the event id
     * @param place    the place
     * @param category the category
     * @return the ticket
     */
    MongoTicket bookTicket(String userId, String eventId, int place, Category category);

    /**
     * Gets booked tickets.
     *
     * @param user     the user
    * @return the booked tickets
     */
    List<MongoTicket> getBookedTickets(MongoUser user);

    /**
     * Gets booked tickets.
     *
     * @param event    the event
     * @return the booked tickets
     */
    List<MongoTicket> getBookedTickets(MongoEvent event);

    /**
     * Cancel ticket boolean.
     *
     * @param ticketId the ticket id
     * @return the boolean
     */
    boolean cancelTicket(String ticketId);

    /**
     * Save all tickets
     *
     * @param tickets list of tickets to save
     * @return flag whether the saving is success
     */
    boolean saveAll(List<MongoTicket> tickets);
}