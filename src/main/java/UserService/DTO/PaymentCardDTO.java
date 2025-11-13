package UserService.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentCardDTO {

    private Long id;

    @EqualsAndHashCode.Exclude
    private Long userId;

    @NotBlank(message = "Payment card number shouldn't be blank")
    @Size(min = 16, max = 16, message = "Credit card should have 16 digits number")
    private String number;

    private String holder;

    //TODO Think about should it be in future or not
    @Future(message = "Expiration date should be in future")

    private Date expirationDate;

    @Column(name = "active")
    private boolean active;

}
