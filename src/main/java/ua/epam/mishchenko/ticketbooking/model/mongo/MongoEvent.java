package ua.epam.mishchenko.ticketbooking.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document("events")
@Data
@AllArgsConstructor
@Builder
@ToString
public class MongoEvent {

    @Id
    private String id;

    private long eventId;

    private String title;

    private Date date;

    private BigDecimal ticketPrice;
}
