package UserService.service;

import UserService.DTO.PageDTO;
import UserService.DTO.PaymentCardDTO;
import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.entity.PaymentCard;
import UserService.entity.User;
import UserService.exception.BusinessRuleConstraintViolationException;
import UserService.exception.EntityAlreadyExistException;
import UserService.exception.EntityNotFoundException;
import UserService.mapper.CycleAvoidingMappingContext;
import UserService.mapper.PaymentCardMapper;
import UserService.mapper.UserMapper;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import UserService.specification.UserFilterRequest;
import UserService.specification.UserSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserServiceImpl implements IUserService {

    UserMapper userMapper;
    PaymentCardMapper paymentCardMapper;
    UserRepository userRepository;
    PaymentCardRepository paymentCardRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PaymentCardMapper paymentCardMapper, UserRepository userRepository, PaymentCardRepository paymentCardRepository) {
        this.userMapper = userMapper;
        this.paymentCardMapper = paymentCardMapper;
        this.userRepository = userRepository;
        this.paymentCardRepository = paymentCardRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "UserDTO", key = "#result.id")
    public UserDTO addUser(@NotNull @Valid UserDTO userDTO) {
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            throw new BusinessRuleConstraintViolationException(String.format("User with email %s already exists. Email should be unique", user.getEmail()));
        });
        User user = userMapper.toEntity(userDTO);
        return userMapper.toUserDTO(this.userRepository.save(user));
    }

    @Override
    @Cacheable(value = "UserDTO", key = "#userId")
    public UserDTO getUserById(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString()));
        return userMapper.toUserDTO(user);
    }

    @Override
    @Cacheable(value = "UserDTOWithCards", key = "#userId")
    public UserDTOWIthCards getUserWithCardsById(Long userId) {
        User user = this.userRepository.findWithCardsById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString()));
        return userMapper.toUserDTOWithCards(user, new CycleAvoidingMappingContext());
    }

    @Override
    public PageDTO<UserDTO> getAllUsers(Pageable pageable) {
        return userMapper.toUserDTOPage(userRepository.findAll(pageable).map(userMapper::toUserDTO));
    }

    @Override
    public PageDTO<UserDTO> getAllUsersFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(userFilterRequest.toSpecification(), pageable);
        return userMapper.toUserDTOPage(userPage.map(user -> userMapper.toUserDTO(user)));
    }


    @Transactional
    @CachePut(value = "UserDTOWithCards", key = "#result.id")
    public UserDTOWIthCards addPaymentCardToUser(Long userId, @NotNull @Valid PaymentCardDTO paymentCardDTO) {
        User user = userRepository.findWithCardsById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString()));
        paymentCardRepository.findByNumber(paymentCardDTO.getNumber()).ifPresent(paymentCard -> {
            throw new EntityAlreadyExistException(String.format("Payment card with number=%s  already exists", paymentCard.getNumber()));
        });
        PaymentCard paymentCard = paymentCardMapper.toEntity(paymentCardDTO);
        paymentCard.setHolder(user.getName().toUpperCase() + user.getSurname().toUpperCase());
        user.addPaymentCard(paymentCard);
        return userMapper.toUserDTOWithCards(userRepository.save(user), new CycleAvoidingMappingContext());
    }

    public PageDTO<UserDTOWIthCards> getAllUsersWithCardsFilteredBy(UserFilterRequest userFilterRequest, Pageable pageable) {
        Specification<User> specification = userFilterRequest.toSpecification().and(UserSpecification.withPaymentCards());
        Page<User> userPage = this.userRepository.findAll(specification, pageable);
        return userMapper.toUserDTOWithCardsPage(userPage.map(user -> userMapper.toUserDTOWithCards(user, new CycleAvoidingMappingContext())));
    }

    @Override
    @Transactional
    @CachePut(value = "UserDTO", key = "#result.id")
    public UserDTO updateUserById(Long userId, @NotNull @Valid UserDTO userDTO) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString()));
        userMapper.updateUserFromDTO(userDTO, userToUpdate);
        return userMapper.toUserDTO(userRepository.save(userToUpdate));
    }

    @Override
    @Transactional
    @CachePut(value = "UserDTO", key = "#result.id")
    public UserDTO deactivateUserById(Long userId) {
        userRepository.deactivateUserById(userId);
        return userMapper.toUserDTO(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString())));
    }

    @Override
    @Transactional
    @CachePut(value = "userDTO", key = "#result.id")
    public UserDTO activateUserById(Long userId) {
        userRepository.activateUserById(userId);
        return userMapper.toUserDTO(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId.toString())));

    }
}
