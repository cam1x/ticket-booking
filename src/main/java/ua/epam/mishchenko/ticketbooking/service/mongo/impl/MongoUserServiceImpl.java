package ua.epam.mishchenko.ticketbooking.service.mongo.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;
import ua.epam.mishchenko.ticketbooking.repository.mongo.MongoUserRepository;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MongoUserServiceImpl implements MongoUserService {
    private static final Logger log = LoggerFactory.getLogger(MongoUserServiceImpl.class);

    private final MongoUserRepository repository;

    private final MongoTemplate template;

    @Override
    public MongoUser getUserById(String id) {
        log.info("Finding a user by id: {}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    public MongoUser getUserByEmail(String email) {
        log.info("Finding a user by email: {}", email);
        if (email.isEmpty()) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return template.findOne(query, MongoUser.class);
    }

    @Override
    public List<MongoUser> getUsersByName(String name) {
        log.info("Finding all users by name {} ", name);
        if (name.isEmpty()) {
            return new ArrayList<>();
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return template.find(query, MongoUser.class);
    }

    public MongoUser getUserByUserId(Long userId) {
        MongoUser example = MongoUser.builder()
                .userId(userId)
                .build();
        return repository.findOne(Example.of(example)).orElse(null);
    }

    @Override
    public MongoUser createUser(MongoUser user) {
        log.info("Start creating an user: {}", user);
        return repository.save(user);
    }

    @Override
    public MongoUser updateUser(MongoUser user) {
        log.info("Start updating an user: {}", user);
        return repository.save(user);
    }

    @Override
    public boolean deleteUser(String id) {
        log.info("Start deleting an user with id: {}", id);
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    public boolean saveAll(List<MongoUser> users) {
        log.info("Start saving a collection of the users");
        if (!Objects.isNull(users) && !users.isEmpty()) {
            List<MongoUser> saved = repository.saveAll(users);
            return !Objects.isNull(saved) && users.size() == saved.size();
        } else {
            log.warn("Can not find a list of users to save");
            return true;
        }
    }

    public List<MongoUser> getAllUsers() {
        return repository.findAll();
    }
}
