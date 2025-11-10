package UserService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTOWIthCards {

    private Long id;

    @NotBlank(message = "Name shouldn't be empty")
    @Size(min = 3, max = 100, message = "Name length should be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Surname shouldn't be empty")
    @Size(min = 3, max = 100, message = "Surname length should be between 3 and 100 characters")
    private String surname;

    @Past(message = "Birth date should be in past")
    private Date birthDate;

    @Email(message = "Email should be valid")
    @Size(min = 6, max = 255, message = "Email length should be between 6 and 255 characters")
    private String email;

    @EqualsAndHashCode.Exclude
    private List<PaymentCardDTO> cards;

    private boolean active;

}
