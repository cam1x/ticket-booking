package ua.epam.mishchenko.ticketbooking.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MongoUserAccount {

    /**
     * The amount of user money.
     */
    private BigDecimal money;
}