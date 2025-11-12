package UserService.service;

import UserService.DTO.PageDTO;
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

    public PageDTO<UserDTO> getAllUsers(Pageable pageable);

    public PageDTO<UserDTO> getAllUsersFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable);

    public PageDTO<UserDTOWIthCards> getAllUsersWithCardsFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable);

    public UserDTO updateUserById(Long userId, UserDTO userDTO);

    public UserDTO deactivateUserById(Long userId);

    public UserDTO activateUserById(Long userId);

}
