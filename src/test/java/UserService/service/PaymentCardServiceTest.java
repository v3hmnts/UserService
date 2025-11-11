package UserService.service;

import UserService.DTO.PaymentCardDTO;
import UserService.entity.PaymentCard;
import UserService.exception.BusinessRuleConstraintViolationException;
import UserService.exception.EntityNotFoundException;
import UserService.mapper.PaymentCardMapper;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class PaymentCardServiceTest {
    @Spy
    private PaymentCardMapper mapper = Mappers.getMapper(PaymentCardMapper.class);

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    @Test
    void addPaymentCard_shouldThrowBusinessRuleConstraintViolationException(){
        // Arrange
        PaymentCard paymentCard = new PaymentCard();
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO();
        paymentCardDTO.setNumber("1111111111111111");
        when(paymentCardRepository.findByNumber(any(String.class))).thenReturn(Optional.of(paymentCard));

        // Act & Assert
        assertThrows(BusinessRuleConstraintViolationException.class,()->paymentCardService.addPaymentCard(paymentCardDTO));
    }

    @Test
    void getPaymentCardById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentPaymentCardId = 1L;
        when(paymentCardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->paymentCardService.getPaymentCardById(nonExistentPaymentCardId));
    }

    @Test
    void getAllPaymentCardsByUserId_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        when(userRepository.findWithCardsById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->paymentCardService.getAllPaymentCardsByUserId(nonExistentUserId));
    }

    @Test
    void updatePaymentCardById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentPaymentCardId = 1L;
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO();
        paymentCardDTO.setId(1L);
        when(paymentCardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->paymentCardService.updatePaymentCardById(nonExistentPaymentCardId,paymentCardDTO));
    }

    @Test
    void deactivatePaymentCardById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentPaymentCardId = 1L;
        when(paymentCardRepository.deactivateCardById(any(Long.class))).thenReturn(0);
        when(paymentCardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->paymentCardService.deactivatePaymentCardById(nonExistentPaymentCardId));
    }

    @Test
    void activatePaymentCardById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentPaymentCardId = 1L;
        when(paymentCardRepository.activateCardById(any(Long.class))).thenReturn(0);
        when(paymentCardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->paymentCardService.activatePaymentCardById(nonExistentPaymentCardId));
    }

}
