package UserService.service;

import UserService.DTO.UserDTO;
import UserService.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface IUserService {
    public UserDTO addUser(UserDTO userDTO);
    public UserDTO getUserById(UUID userId);
    public Page<UserDTO> getAllUsers(Pageable pageable, Specification<User> specification);
    public void updateUser(UserDTO userDTO);
    public void deactivateUserById(UUID userId);
    public void activateUserById(UUID userId);
}
