package UserService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name shouldn't be empty")
    @Size(min = 3, max = 100, message = "Name length should be between 3 and 100 characters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Surname shouldn't be empty")
    @Size(min = 3, max = 100, message = "Surname length should be between 3 and 100 characters")
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = "Birth date is mandatory")
    @Column(name = "birth_date")
    private Date birthDate;

    @Email(message = "Email should be valid")
    @Size(min = 6, max = 255, message = "Email length should be between 6 and 255 characters")
    @Column(unique = true, name = "email")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<PaymentCard> cards = new ArrayList<>();

    @Column(name = "active")
    private boolean active;

    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Timestamp updatedAt;


    public void addPaymentCard(PaymentCard paymentCard) throws Exception {
        if(cards.size()>=5){
            throw new Exception("User should have no more than 5 payment cards");
        }
        cards.add(paymentCard);
        paymentCard.setUser(this);
    }

    public void removePaymentCard(PaymentCard paymentCard){
        cards.remove(paymentCard);
        paymentCard.setUser(null);
    }

}
