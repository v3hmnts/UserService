package UserService.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.UUID;

@Data
public class PaymentCardDTO {

    private UUID id;

    @EqualsAndHashCode.Exclude
    private Long userId;

    @NotBlank(message = "Payment card number shouldn't be blank")
    @Size(min = 16,max = 16,message = "Credit card should have 16 digits number")
    private String number;

    @NotBlank(message = "Holder name and surname is mandatory")
    private String holder;

    //TODO Think about should it be in future or not
    @Future
    private Date expirationDate;

    @Column(name = "active")
    private boolean active;

}
