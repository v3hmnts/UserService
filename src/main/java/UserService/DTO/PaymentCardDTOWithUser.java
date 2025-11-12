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
public class PaymentCardDTOWithUser {

    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserDTO user;

    @NotBlank(message = "Payment card number shouldn't be blank")
    @Size(min = 16, max = 16, message = "Credit card should have 16 digits number")
    private String number;

    @NotBlank(message = "Holder name and surname is mandatory")
    private String holder;

    //TODO Think about should it be in future or not
    @Future
    private Date expirationDate;

    @Column(name = "active")
    private boolean active;
}
