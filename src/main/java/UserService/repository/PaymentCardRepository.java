package UserService.repository;

import UserService.entity.PaymentCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>, JpaSpecificationExecutor<PaymentCard> {

    @Query(value = "select pc.* from payment_cards pc where pc.user_id=:userId", nativeQuery = true)
    public List<PaymentCard> findAllPaymentCardsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT pc FROM PaymentCard pc LEFT JOIN FETCH pc.user", countQuery = "SELECT COUNT(pc) FROM PaymentCard pc")
    public List<PaymentCard> findAllWithUsers(Pageable pageable);

    @Modifying
    @Query(value = "UPDATE PaymentCard pc SET pc.active=false WHERE pc.id=:paymentCardId")
    public int deactivateCardById(@Param("paymentCardId") Long paymentCardId);

    @Modifying
    @Query(value = "UPDATE PaymentCard pc SET pc.active=true WHERE pc.id=:paymentCardId")
    public int activateCardById(@Param("paymentCardId") Long paymentCardId);

    public Optional<PaymentCard> findByNumber(String cardNumber);

}
