package UserService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "payment_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@ToString
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @Digits(integer = 16, message = "Credit card should have 16 digits number", fraction = 0)
    @Min(value = 1000000000000000L, message = "Credit card should have 16 digits number")
    @Max(value = 9999999999999999L, message = "Credit card should have 16 digits number")
    @Column(unique = true, name = "number")
    private long number;

    @NotBlank(message = "Holder name and surname is mandatory")
    @Column(name = "holder")
    private String holder;

    @Future
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "active")
    private boolean active;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Timestamp updatedAt;

}
