package ua.epam.mishchenko.ticketbooking.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The type User.
 */
@Document("users")
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MongoUser {

    @Id
    private String id;

    private Long userId;

    private String name;

    private String email;

    @Field("user_account")
    private MongoUserAccount userAccount;
}

