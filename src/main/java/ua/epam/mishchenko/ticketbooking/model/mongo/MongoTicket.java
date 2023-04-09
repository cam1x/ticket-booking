package ua.epam.mishchenko.ticketbooking.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ua.epam.mishchenko.ticketbooking.model.Category;

@Document("tickets")
@Data
@AllArgsConstructor
@Builder
@ToString
public class MongoTicket {

    private String id;

    @Field("user")
    @DBRef
    private MongoUser user;

    private Integer place;

    private Category category;

    @DBRef
    private MongoEvent event;
}
