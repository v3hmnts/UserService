package UserService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "payment_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Size(min = 16, max = 16, message = "Credit card should have 16 digits number")
    @Column(unique = true, name = "number")
    private String number;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof PaymentCard))
            return false;

        PaymentCard other = (PaymentCard) obj;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
