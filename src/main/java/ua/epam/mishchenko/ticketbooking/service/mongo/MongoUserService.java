package ua.epam.mishchenko.ticketbooking.service.mongo;



import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;

import java.util.List;

public interface MongoUserService {
    /**
     * Gets MongoUser by id.
     *
     * @param userId the MongoUser id
     * @return the MongoUser by id
     */
    MongoUser getUserById(String userId);

    /**
     * Get User By UserId
     *
     * @param userId as Long
     * @return user as MongoUser
     */
    MongoUser getUserByUserId(Long userId);
    /**
     * Gets User by email.
     *
     * @param email the email
     * @return the user by email
     */
    MongoUser getUserByEmail(String email);

    /**
     * Gets MongoUsers by name.
     *
     * @param name     the name
      * @return the users by name
     */
    List<MongoUser> getUsersByName(String name);

    /**
     * Gets all users
     * @return the users
     */
    List<MongoUser> getAllUsers();

    /**
     * Create User .
     *
     * @param user the MongoUser
     * @return the MongoUser
     */
    MongoUser createUser(MongoUser user);

    /**
     * Save all users
     *
     * @param users list of users
     * @return flag whether the saving is success
     */
    boolean saveAll(List<MongoUser> users);

    /**
     * Update user MongoUser.
     *
     * @param user the MongoUser
     * @return the MongoUser
     */
    MongoUser updateUser(MongoUser user);

    /**
     * Delete user boolean.
     *
     * @param userId the MongoUser id
     * @return the boolean
     */
    boolean deleteUser(String userId);

}
