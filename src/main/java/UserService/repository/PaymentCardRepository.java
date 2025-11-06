package UserService.repository;

import UserService.entity.PaymentCard;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long>, JpaSpecificationExecutor<PaymentCard> {

    @Query("select pc from PaymentCard pc where pc.user.id=?1")
    public List<PaymentCard> getAllPaymentCardsByUserId(Long userId);

    @Query(value = "select pc.* from payment_cards pc join users u on pc.user_id=u.id",nativeQuery = true)
    public List<PaymentCard> findAllWithUsers();

}
