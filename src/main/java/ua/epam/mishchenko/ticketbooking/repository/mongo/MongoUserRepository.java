package ua.epam.mishchenko.ticketbooking.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;

@Repository
public interface MongoUserRepository extends MongoRepository<MongoUser, String> {


}

