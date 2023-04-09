package ua.epam.mishchenko.ticketbooking.service.mongo;

import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;

import java.util.Date;
import java.util.List;


public interface MongoEventService {

    /**
     * Get Event By id
     * @param id
     * @return the event
     */
     MongoEvent getEventById(String id);

    /**
     * Get Event By EventId
     *
     * @param eventId
     * @return the event
     */
    MongoEvent getEventByEventId(long eventId);

    /**
     * Gets events by title.
     *
     * @param title    the title
      * @return the events by title
     */
    List<MongoEvent> getEventsByTitle(String title);

    /**
     * Gets events for day.
     *
     * @param day      the day
     * @return the events for day
     */
    List<MongoEvent> getEventsForDay(Date day);

    /**
     * Create event MongoEvent.
     *
     * @param event the event
     * @return the event
     */
    MongoEvent createEvent(MongoEvent event);

    /**
     * Update event MongoEvent.
     *
     * @param event the event
     * @return the event
     */
    MongoEvent updateEvent(MongoEvent event);

    /**
     * Delete event boolean.
     *
     * @param eventId the event id
     * @return the boolean
     */
    boolean deleteEvent(String eventId);

    /**
     *
     * Get All Events
     * @param events
     * @return all events
     */
    boolean saveAll(List<MongoEvent> events);
}
