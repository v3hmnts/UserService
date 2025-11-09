package UserService.controller;

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
    public ResponseEntity<UserDTO> getUserById(@PathVariable(name = "userId") UUID userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/{userId}/with-cards")
    public ResponseEntity<UserDTOWIthCards> getUserWithCardsById(@PathVariable UUID userId) {
        UserDTOWIthCards userWithCards = userService.getUserWithCardsById(userId);
        return ResponseEntity.ok(userWithCards);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<UserDTO>> getAllUsersFilteredBy(
            @Valid UserFilterRequest userFilterRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsersFilteredBy(userFilterRequest, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/filtered/with-cards")
    public ResponseEntity<Page<UserDTOWIthCards>> getAllUsersWithCardsFilteredBy(
            @Valid UserFilterRequest userFilterRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTOWIthCards> users = userService.getAllUsersWithCardsFilteredBy(userFilterRequest, pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(
            @PathVariable UUID userId,
            @Valid @RequestBody UserDTO userDTO) throws Exception {
        userService.updateUserById(userId, userDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUserById(@PathVariable UUID userId) {
        userService.deactivateUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUserById(@PathVariable UUID userId) {
        userService.activateUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/cards")
    public ResponseEntity<List<PaymentCardDTO>> getAllPaymentCardsByUserId(@PathVariable UUID userId) {
        List<PaymentCardDTO> paymentCards = paymentCardService.getAllPaymentCardsByUserId(userId);
        return ResponseEntity.ok(paymentCards);
    }

    @PostMapping("/{userId}/cards")
    @Transactional
    public ResponseEntity<UserDTOWIthCards> addPaymentCard(@PathVariable UUID userId, @NotNull @Valid @RequestBody PaymentCardDTO paymentCardDTO) {
        UserDTOWIthCards userDTO = userService.addPaymentCardToUser(userId, paymentCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
}
