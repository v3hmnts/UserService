package UserService.service;

import UserService.DTO.PaymentCardDTO;
import UserService.specification.PaymentCardFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPaymentCardService {
    public PaymentCardDTO addPaymentCard(PaymentCardDTO paymentCardDTO);

    public PaymentCardDTO getPaymentCardById(UUID paymentCardId);

    public Page<PaymentCardDTO> getAllPaymentCardsFilteredBy(PaymentCardFilterRequest paymentCardFilterRequest, Pageable pageable);

    public List<PaymentCardDTO> getAllPaymentCardsByUserId(UUID userId);

    public PaymentCardDTO updatePaymentCardById(UUID paymentCardId, PaymentCardDTO paymentCardDTO);

    public void deactivatePaymentCardById(UUID paymentCardId);

    public void activatePaymentCardById(UUID paymentCardId);

}
