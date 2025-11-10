package UserService.service;

import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.specification.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {

    public UserDTO addUser(UserDTO userDTO);

    public UserDTO getUserById(UUID userId);

    public UserDTOWIthCards getUserWithCardsById(UUID userId) ;

    public Page<UserDTO> getAllUsers(Pageable pageable);

    public Page<UserDTO> getAllUsersFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable);

    public Page<UserDTOWIthCards> getAllUsersWithCardsFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable);

    public UserDTO updateUserById(UUID userId, UserDTO userDTO);

    public void deactivateUserById(UUID userId);

    public void activateUserById(UUID userId);


}
