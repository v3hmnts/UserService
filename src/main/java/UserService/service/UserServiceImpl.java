package UserService.service;

import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.entity.User;
import UserService.exception.EntityNotFoundException;
import UserService.mapper.CycleAvoidingMappingContext;
import UserService.mapper.UserMapper;
import UserService.repository.UserRepository;
import UserService.specification.UserFilterRequest;
import UserService.specification.UserSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    UserMapper userMapper;
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDTO addUser(@NotNull @Valid UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toUserDTO(this.userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(UUID userId){
        User user = this.userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s not found",userId)));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTOWIthCards getUserWithCardsById(UUID userId){
        User user = this.userRepository.findWithCardsById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s not found",userId)));
        return userMapper.toUserDTOWithCards(user, new CycleAvoidingMappingContext());
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable).map(userMapper::toUserDTO);
    }

    @Override
    public Page<UserDTO> getAllUsersFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(userFilterRequest.toSpecification(), pageable);
        return userPage.map(user -> userMapper.toUserDTO(user));
    }

    public Page<UserDTOWIthCards> getAllUsersWithCardsFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable) {
        Specification<User> specification = userFilterRequest.toSpecification().and(UserSpecification.withPaymentCards());
        Page<User> userPage = this.userRepository.findAll(specification, pageable);
        return userPage.map(user -> userMapper.toUserDTOWithCards(user, new CycleAvoidingMappingContext()));
    }

    @Override
    @Transactional
    public UserDTO updateUserById(UUID userId, @NotNull @Valid UserDTO userDTO) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s not found",userId)));
        userMapper.updateUserFromDTO(userDTO, userToUpdate);
        return userMapper.toUserDTO(userRepository.save(userToUpdate));
    }

    @Override
    @Transactional
    public void deactivateUserById(UUID userId) {
        userRepository.deactivateUserById(userId);
    }

    @Override
    @Transactional
    public void activateUserById(UUID userId) {
        userRepository.activateUserById(userId);
    }
}
