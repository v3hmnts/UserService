package UserService.service;

import UserService.DTO.PaymentCardDTO;
import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.specification.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {

    public UserDTO addUser(UserDTO userDTO);

    public UserDTOWIthCards addPaymentCardToUser(Long userId, PaymentCardDTO paymentCardDTO);

    public UserDTO getUserById(Long userId);

    public UserDTOWIthCards getUserWithCardsById(Long userId);

    public Page<UserDTO> getAllUsers(Pageable pageable);

    public Page<UserDTO> getAllUsersFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable);

    public Page<UserDTOWIthCards> getAllUsersWithCardsFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable);

    public UserDTO updateUserById(Long userId, UserDTO userDTO);

    public UserDTO deactivateUserById(Long userId);

    public UserDTO activateUserById(Long userId);

}
