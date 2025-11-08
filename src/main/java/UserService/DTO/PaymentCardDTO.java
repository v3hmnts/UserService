package UserService.DTO;

import UserService.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class PaymentCardDTO {

    private UUID id;

    private UserDTO user;

    @Digits(integer = 16, message = "Credit card should have 16 digits number", fraction = 0)
    @Min(value = 1000000000000000L, message = "Credit card should have 16 digits number")
    @Max(value = 9999999999999999L, message = "Credit card should have 16 digits number")
    private long number;

    @NotBlank(message = "Holder name and surname is mandatory")
    private String holder;

    //TODO Think about should it be in future or not
    @Future
    private Date expirationDate;

    @Column(name = "active")
    private boolean active;

}
