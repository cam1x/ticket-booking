package ua.epam.mishchenko.ticketbooking.service.mongo.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoEvent;
import ua.epam.mishchenko.ticketbooking.repository.mongo.MongoEventRepository;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoEventService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MongoEventServiceImpl implements MongoEventService {

    private static final Logger log = LoggerFactory.getLogger(MongoEventServiceImpl.class);

    private final MongoEventRepository repository;

    private final MongoTemplate template;

    @Override
    public MongoEvent getEventById(String id) {
        log.info("Finding an event by id: {}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    public MongoEvent getEventByEventId(long eventId) {
        log.info("Finding an event by id: {}", eventId);
        MongoEvent example = MongoEvent.builder()
                .eventId(eventId)
                .build();
        return repository.findOne(Example.of(example)).orElse(null);
    }

    @Override
    public List<MongoEvent> getEventsByTitle(String title) {
        if (title.isEmpty()) {
            return new ArrayList<>();
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        return template.find(query, MongoEvent.class);
    }

    @Override
    public List<MongoEvent> getEventsForDay(Date day) {
        log.info("Finding all events for day {} ", day);
        if (day == null) {
            return new ArrayList<>();
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("date").is(day));
        return template.find(query, MongoEvent.class);
    }

    @Override
    public MongoEvent createEvent(MongoEvent event) {
        log.info("Start creating an event: {}", event);
        if (Objects.isNull(event)) {
            return null;
        }
        return repository.insert(event);
    }

    @Override
    public MongoEvent updateEvent(MongoEvent event) {
        log.info("Start updating an event: {}", event);
        if (Objects.isNull(event)) {
            return null;
        }
        return repository.save(event);
    }

    @Override
    public boolean deleteEvent(String id) {
        log.info("Start deleting an event with id: {}", id);
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    public boolean saveAll(List<MongoEvent> events) {
        log.info("Start saving a collection of the events");
        if (!Objects.isNull(events) && !events.isEmpty()) {
            List<MongoEvent> saved = repository.saveAll(events);
            return !Objects.isNull(saved) && events.size() == saved.size();
        } else {
            log.warn("Can not to find a list of events to save");
            return true;
        }
    }
}