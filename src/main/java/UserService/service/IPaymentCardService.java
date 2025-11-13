package UserService.service;

import UserService.DTO.PageDTO;
import UserService.DTO.PaymentCardDTO;
import UserService.specification.PaymentCardFilterRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPaymentCardService {
    public PaymentCardDTO addPaymentCard(PaymentCardDTO paymentCardDTO);

    public PaymentCardDTO getPaymentCardById(Long paymentCardId);

    public PageDTO<PaymentCardDTO> getAllPaymentCardsFilteredBy(PaymentCardFilterRequest paymentCardFilterRequest, Pageable pageable);

    public List<PaymentCardDTO> getAllPaymentCardsByUserId(Long userId);

    public PaymentCardDTO updatePaymentCardById(Long paymentCardId, PaymentCardDTO paymentCardDTO);

    public PaymentCardDTO deactivatePaymentCardById(Long paymentCardId);

    public PaymentCardDTO activatePaymentCardById(Long paymentCardId);

}
