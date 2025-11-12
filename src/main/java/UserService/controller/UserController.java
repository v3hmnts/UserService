package UserService.controller;

import UserService.DTO.PageDTO;
import UserService.DTO.PaymentCardDTO;
import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.service.IPaymentCardService;
import UserService.service.IUserService;
import UserService.specification.UserFilterRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    IUserService userService;
    IPaymentCardService paymentCardService;

    @Autowired
    public UserController(IUserService userService, IPaymentCardService paymentCardService) {
        this.userService = userService;
        this.paymentCardService = paymentCardService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@NotNull @Valid @RequestBody UserDTO userDTO) {
        UserDTO userDTOtoReturn = userService.addUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTOtoReturn);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(name = "userId") Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/{userId}/with-cards")
    public ResponseEntity<UserDTOWIthCards> getUserWithCardsById(@PathVariable Long userId) {
        UserDTOWIthCards userWithCards = userService.getUserWithCardsById(userId);
        return ResponseEntity.ok(userWithCards);
    }

    @GetMapping
    public ResponseEntity<PageDTO<UserDTO>> getAllUsers(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageDTO<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/filtered")
    public ResponseEntity<PageDTO<UserDTO>> getAllUsersFilteredBy(
            @Valid UserFilterRequest userFilterRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageDTO<UserDTO> users = userService.getAllUsersFilteredBy(userFilterRequest, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/filtered/with-cards")
    public ResponseEntity<PageDTO<UserDTOWIthCards>> getAllUsersWithCardsFilteredBy(
            @Valid UserFilterRequest userFilterRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageDTO<UserDTOWIthCards> users = userService.getAllUsersWithCardsFilteredBy(userFilterRequest, pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUserById(
            @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO) throws Exception {
        UserDTO userDTOtoReturn = userService.updateUserById(userId, userDTO);
        return ResponseEntity.ok(userDTOtoReturn);
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<UserDTO> deactivateUserById(@PathVariable Long userId) {
        UserDTO deactivatedUserDTO = userService.deactivateUserById(userId);
        return ResponseEntity.ok(deactivatedUserDTO);
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<UserDTO> activateUserById(@PathVariable Long userId) {
        UserDTO deactivatedUserDTO = userService.activateUserById(userId);
        return ResponseEntity.ok(deactivatedUserDTO);
    }

    @GetMapping("/{userId}/cards")
    public ResponseEntity<List<PaymentCardDTO>> getAllPaymentCardsByUserId(@PathVariable Long userId) {
        List<PaymentCardDTO> paymentCards = paymentCardService.getAllPaymentCardsByUserId(userId);
        return ResponseEntity.ok(paymentCards);
    }

    @PostMapping("/{userId}/cards")
    @Transactional
    public ResponseEntity<UserDTOWIthCards> addPaymentCard(@PathVariable Long userId, @NotNull @Valid @RequestBody PaymentCardDTO paymentCardDTO) {
        UserDTOWIthCards userDTO = userService.addPaymentCardToUser(userId, paymentCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
}
