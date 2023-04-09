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
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUser;
import ua.epam.mishchenko.ticketbooking.model.mongo.MongoUserAccount;
import ua.epam.mishchenko.ticketbooking.service.mongo.MongoUserService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class MongoUsersController {

    private static final Logger log = LoggerFactory.getLogger(MongoUsersController.class);

    private final MongoUserService service;

    @GetMapping("/{id}")
    public ModelAndView showUserById(@PathVariable String id) {
        log.info("Showing user by id: {}", id);
        Map<String, Object> model = new HashMap<>();
        MongoUser userById = service.getUserById(id);
        if (Objects.isNull(userById)) {
            model.put("message", "Can not find user by id: " + id);
        } else {
            model.put("user", userById);
        }
        return new ModelAndView("user", model);
    }

    @GetMapping("/name/{name}")
    public ModelAndView showUsersByName(@PathVariable String name) {
        log.info("Showing users by name: {}", name);
        Map<String, Object> model = new HashMap<>();
        List<MongoUser> usersByName = service.getUsersByName(name);
        if (usersByName.isEmpty()) {
            model.put("message", "Can not find users by name: " + name);
        } else {
            model.put("users", usersByName);
        }
        return new ModelAndView("users", model);
    }

    @GetMapping("/email/{email}")
    public ModelAndView showUserByEmail(@PathVariable String email) {
        log.info("Showing the user by email: {}", email);
        Map<String, Object> model = new HashMap<>();
        MongoUser userByEmail = service.getUserByEmail(email);
        if (Objects.isNull(userByEmail)) {
            model.put("message", "Can not to find user by email: " + email);
        } else {
            model.put("user", userByEmail);
        }
        return new ModelAndView("user", model);
    }

    @GetMapping
    public ModelAndView findAll() {
        log.info("Showing all users");
        Map<String, Object> model = new HashMap<>();
        List<MongoUser> users = service.getAllUsers();
        if (Objects.isNull(users)) {
            model.put("message", "Can not to find any users");
        } else {
            model.put("users", users);
        }
        return new ModelAndView("users", model);
    }

    @PostMapping
    public ModelAndView createUser(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam BigDecimal money) {
        MongoUser user = MongoUser.builder().name(name).email(email)
                .userAccount(MongoUserAccount.builder().money(money).build())
                .build();
        log.info("Creating user ", user.toString());
        Map<String, Object> model = new HashMap<>();
        MongoUser createdUser = service.createUser(user);
        if (Objects.isNull(createdUser)) {
            model.put("message",
                    "Can not create user with name - " + user.getName() + " and email - " + user.getEmail());
        } else {
            model.put("user", createdUser);
            log.info("The user successfully created");
        }
        return new ModelAndView("user", model);
    }

    @PutMapping
    public ModelAndView updateUser(@RequestParam String id,
                                   @RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam BigDecimal money) {
        MongoUser user = MongoUser.builder().id(id).name(name).email(email)
                .userAccount(MongoUserAccount.builder().money(money).build())
                .build();
        log.info("Updating user with id: {}", user.getId());
        Map<String, Object> model = new HashMap<>();
        MongoUser updatedUser = service.updateUser(user);
        if (Objects.isNull(updatedUser)) {
            model.put("message", "Can not to update user with id: " + user.getId());
        } else {
            model.put("user", updatedUser);
        }
        return new ModelAndView("user", model);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteUser(@PathVariable String id) {
        log.info("Deleting the user with id: {}", id);
        Map<String, Object> model = new HashMap<>();
        boolean isUserRemoved = service.deleteUser(id);
        if (isUserRemoved) {
            model.put("message", "The user with id: " + id + " successfully removed");
        } else {
            model.put("message", "The user with id: " + id + " not removed");
        }
        return new ModelAndView("user", model);
    }
}
