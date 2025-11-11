package UserService.service;

import UserService.DTO.PaymentCardDTO;
import UserService.DTO.UserDTO;
import UserService.builders.UserBuilder;
import UserService.entity.User;
import UserService.exception.BusinessRuleConstraintViolationException;
import UserService.exception.EntityNotFoundException;
import UserService.mapper.UserMapper;
import UserService.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class UserServiceTest {

    @Spy
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser_shouldThrowBusinessRuleConstraintViolationException() {
        // Arrange
        User expectedUser = new UserBuilder()
                .id(1L)
                .name("TEST")
                .surname("TEST")
                .birthDate(new Date(1768468200000L))
                .email("qqqq@qqq.ru")
                .active(true)
                .build();

        UserDTO requestDTO = new UserDTO();
        requestDTO.setName(expectedUser.getName());
        requestDTO.setSurname(expectedUser.getSurname());
        requestDTO.setBirthDate(expectedUser.getBirthDate());
        requestDTO.setEmail(expectedUser.getEmail());
        requestDTO.setActive(expectedUser.isActive());


        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(BusinessRuleConstraintViolationException.class,()->userService.addUser(requestDTO));

    }

    @Test
    void getUserById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->userService.getUserById(nonExistentUserId));
    }

    @Test
    void getUserWithCardsById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        when(userRepository.findWithCardsById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->userService.getUserWithCardsById(nonExistentUserId));
    }

    @Test
    void addPaymentCardToUser_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO();
        when(userRepository.findWithCardsById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->userService.addPaymentCardToUser(nonExistentUserId,paymentCardDTO));
    }

    @Test
    void updateUserById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        UserDTO userDTO = new UserDTO();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->userService.updateUserById(nonExistentUserId,userDTO));
    }

    @Test
    void deactivateUserById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(userRepository.deactivateUserById(any(Long.class))).thenReturn(0);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->userService.deactivateUserById(nonExistentUserId));
    }

    @Test
    void activateUserById_shouldThrowEntityNotFoundException(){
        // Arrange
        Long nonExistentUserId = 1L;
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(userRepository.activateUserById(any(Long.class))).thenReturn(0);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,()->userService.activateUserById(nonExistentUserId));
    }
}
