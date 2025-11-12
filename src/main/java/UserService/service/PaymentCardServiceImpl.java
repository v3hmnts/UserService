package UserService.service;

import UserService.DTO.PageDTO;
import UserService.DTO.PaymentCardDTO;
import UserService.entity.PaymentCard;
import UserService.entity.User;
import UserService.exception.BusinessRuleConstraintViolationException;
import UserService.exception.EntityNotFoundException;
import UserService.mapper.PaymentCardMapper;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import UserService.specification.PaymentCardFilterRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class PaymentCardServiceImpl implements IPaymentCardService {

    PaymentCardRepository paymentCardRepository;
    PaymentCardMapper paymentCardMapper;
    UserRepository userRepository;

    @Autowired
    public PaymentCardServiceImpl(PaymentCardRepository paymentCardRepository, PaymentCardMapper paymentCardMapper, UserRepository userRepository) {
        this.paymentCardRepository = paymentCardRepository;
        this.paymentCardMapper = paymentCardMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "PaymentCardDTO", key = "#result.id")
    public PaymentCardDTO addPaymentCard(@NotNull @Valid PaymentCardDTO paymentCardDTO) {
        paymentCardRepository.findByNumber(paymentCardDTO.getNumber()).ifPresent(card -> {
            throw new BusinessRuleConstraintViolationException(String.format("Payment card with number = %s already exist. Payment card number should be unique", card.getNumber()));
        });
        PaymentCard paymentCard = paymentCardMapper.toEntity(paymentCardDTO);
        return paymentCardMapper.toPaymentCardDTO(paymentCardRepository.save(paymentCard));
    }

    @Override
    @Cacheable(value = "PaymentCardDTO", key = "#paymentCardId")
    public PaymentCardDTO getPaymentCardById(Long paymentCardId) {
        PaymentCard paymentCard = paymentCardRepository.findById(paymentCardId).orElseThrow(() -> new EntityNotFoundException("Payment Card", paymentCardId.toString()));
        return paymentCardMapper.toPaymentCardDTO(paymentCard);
    }

    @Override
    public PageDTO<PaymentCardDTO> getAllPaymentCardsFilteredBy(PaymentCardFilterRequest paymentCardFilterRequest, Pageable pageable) {
        Page<PaymentCard> paymentCardPage = paymentCardRepository.findAll(paymentCardFilterRequest.toSpecification(), pageable);
        Page<PaymentCardDTO> paymentCardDTOSPage = paymentCardPage.map((paymentCard) -> paymentCardMapper.toPaymentCardDTO(paymentCard));
        return paymentCardMapper.toPaymentCardDTOPage(paymentCardDTOSPage);
    }

    @Override
    public List<PaymentCardDTO> getAllPaymentCardsByUserId(Long userId) {
        User user = userRepository.findWithCardsById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString()));
        return paymentCardMapper.toPaymentCardDTOList(user.getCards());
    }

    @Override
    @Transactional
    @CachePut(value = "PaymentCardDTO", key = "#result.id")
    public PaymentCardDTO updatePaymentCardById(Long paymentCardId, @NotNull @Valid PaymentCardDTO paymentCardDTO) {
        PaymentCard paymentCardToUpdate = paymentCardRepository.findById(paymentCardId).orElseThrow(() -> new EntityNotFoundException("Payment Card", paymentCardId.toString()));
        paymentCardMapper.updateFromDTO(paymentCardDTO, paymentCardToUpdate);
        return paymentCardMapper.toPaymentCardDTO(paymentCardRepository.save(paymentCardToUpdate));
    }

    @Override
    @Transactional
    @CachePut(value = "PaymentCardDTO", key = "#result.id")
    public PaymentCardDTO deactivatePaymentCardById(Long paymentCardId) {
        paymentCardRepository.deactivateCardById(paymentCardId);
        return paymentCardMapper.toPaymentCardDTO(paymentCardRepository.findById(paymentCardId).orElseThrow(() -> new EntityNotFoundException("Payment Card", paymentCardId.toString())));
    }

    @Override
    @Transactional
    @CachePut(value = "PaymentCardDTO", key = "#result.id")
    public PaymentCardDTO activatePaymentCardById(Long paymentCardId) {
        paymentCardRepository.activateCardById(paymentCardId);
        return paymentCardMapper.toPaymentCardDTO(paymentCardRepository.findById(paymentCardId).orElseThrow(() -> new EntityNotFoundException("Payment Card", paymentCardId.toString())));
    }
}
