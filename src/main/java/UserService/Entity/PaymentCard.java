package UserService.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "payment_cards")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PaymentCard {
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "number")
    private long number;

    @Column(name = "holder")
    private String holder;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "active")
    private boolean active;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
