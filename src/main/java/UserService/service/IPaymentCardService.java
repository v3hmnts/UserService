package UserService.service;

import UserService.DTO.PaymentCardDTO;
import UserService.entity.PaymentCard;
import UserService.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface IPaymentCardService {
    public PaymentCardDTO addPaymentCard(PaymentCardDTO paymentCardDTO);
    public PaymentCardDTO getPaymentCardById(UUID paymentCardId);
    public Page<PaymentCardDTO> getAllPaymentCardsFilteredBy(Pageable pageable, Specification<PaymentCard> specification);
    public List<PaymentCard> getAllPaymentCardsByUserId(UUID userId);
    public void updateUser(PaymentCardDTO paymentCardDTO);
    public void deactivatePaymentCardById(UUID paymentCardId);
    public void activatePaymentCardById(UUID paymentCardId);

}
