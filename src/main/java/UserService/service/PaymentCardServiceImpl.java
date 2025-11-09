package UserService.service;

import UserService.DTO.PaymentCardDTO;
import UserService.entity.PaymentCard;
import UserService.exception.EntityNotFoundException;
import UserService.mapper.PaymentCardMapper;
import UserService.repository.PaymentCardRepository;
import UserService.specification.PaymentCardFilterRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class PaymentCardServiceImpl implements IPaymentCardService {

    PaymentCardRepository paymentCardRepository;
    PaymentCardMapper paymentCardMapper;

    @Autowired
    public PaymentCardServiceImpl(PaymentCardRepository paymentCardRepository, PaymentCardMapper paymentCardMapper) {
        this.paymentCardRepository = paymentCardRepository;
        this.paymentCardMapper = paymentCardMapper;
    }


    @Override
    @Transactional
    public PaymentCardDTO addPaymentCard(@NotNull @Valid PaymentCardDTO paymentCardDTO) {
        PaymentCard paymentCard = paymentCardMapper.toEntity(paymentCardDTO);
        return paymentCardMapper.toPaymentCardDTO(paymentCardRepository.save(paymentCard));
    }


    @Override
    public PaymentCardDTO getPaymentCardById(UUID paymentCardId) {
        PaymentCard paymentCard = paymentCardRepository.findById(paymentCardId).orElseThrow(() -> new EntityNotFoundException("Payment Card", paymentCardId.toString()));
        return paymentCardMapper.toPaymentCardDTO(paymentCard);
    }

    @Override
    public Page<PaymentCardDTO> getAllPaymentCardsFilteredBy(PaymentCardFilterRequest paymentCardFilterRequest, Pageable pageable) {
        Page<PaymentCard> paymentCardPage = paymentCardRepository.findAll(paymentCardFilterRequest.toSpecification(), pageable);
        return paymentCardPage.map((paymentCard) -> paymentCardMapper.toPaymentCardDTO(paymentCard));
    }

    @Override
    public List<PaymentCardDTO> getAllPaymentCardsByUserId(UUID userId) {
        List<PaymentCard> paymentCards = paymentCardRepository.findAllPaymentCardsByUserId(userId);
        return paymentCardMapper.toPaymentCardDTOList(paymentCards);
    }

    @Override
    @Transactional
    public PaymentCardDTO updatePaymentCardById(UUID paymentCardId, @NotNull @Valid PaymentCardDTO paymentCardDTO) {
        PaymentCard paymentCardToUpdate = paymentCardRepository.findById(paymentCardId).orElseThrow(() -> new EntityNotFoundException("Payment Card", paymentCardId.toString()));
        paymentCardMapper.updateFromDTO(paymentCardDTO, paymentCardToUpdate);
        return paymentCardMapper.toPaymentCardDTO(paymentCardRepository.save(paymentCardToUpdate));
    }

    @Override
    @Transactional
    public void deactivatePaymentCardById(UUID paymentCardId) {
        paymentCardRepository.deactivateCardById(paymentCardId);
    }

    @Override
    @Transactional
    public void activatePaymentCardById(UUID paymentCardId) {
        paymentCardRepository.activateCardById(paymentCardId);
    }
}
