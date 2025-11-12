package UserService.controller;

import UserService.DTO.PageDTO;
import UserService.DTO.PaymentCardDTO;
import UserService.DTO.UserDTO;
import UserService.service.IPaymentCardService;
import UserService.specification.PaymentCardFilterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment-cards")
public class PaymentCardController {

    IPaymentCardService paymentCardService;

    public PaymentCardController(IPaymentCardService paymentCardService) {
        this.paymentCardService = paymentCardService;
    }

    @PostMapping
    public ResponseEntity<PaymentCardDTO> addPaymentCard(@NotNull @Valid @RequestBody PaymentCardDTO paymentCardDTO) {
        PaymentCardDTO paymentCardDTOToReturn = paymentCardService.addPaymentCard(paymentCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentCardDTOToReturn);
    }

    @GetMapping("/{paymentCardId}")
    public ResponseEntity<PaymentCardDTO> getPaymentCardById(@PathVariable Long paymentCardId) {
        PaymentCardDTO paymentCard = paymentCardService.getPaymentCardById(paymentCardId);
        return ResponseEntity.ok(paymentCard);
    }

    @GetMapping("/filtered")
    public ResponseEntity<PageDTO<PaymentCardDTO>> getAllPaymentCardsFilteredBy(
            @Valid PaymentCardFilterRequest paymentCardFilterRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageDTO<PaymentCardDTO> paymentCardDTOS = paymentCardService.getAllPaymentCardsFilteredBy(paymentCardFilterRequest, pageable);
        return ResponseEntity.ok(paymentCardDTOS);
    }

    @PutMapping("/{paymentCardId}")
    public ResponseEntity<PaymentCardDTO> updatePaymentCardById(
            @PathVariable Long paymentCardId,
            @Valid @RequestBody PaymentCardDTO paymentCardDTO) {

        PaymentCardDTO updatedPaymentCard = paymentCardService.updatePaymentCardById(paymentCardId, paymentCardDTO);
        return ResponseEntity.ok(updatedPaymentCard);
    }

    @PatchMapping("/{paymentCardId}/deactivate")
    public ResponseEntity<PaymentCardDTO> deactivatePaymentCardById(@PathVariable Long paymentCardId) {
        PaymentCardDTO paymentCardDTO = paymentCardService.deactivatePaymentCardById(paymentCardId);
        return ResponseEntity.ok(paymentCardDTO);
    }

    @PatchMapping("/{paymentCardId}/activate")
    public ResponseEntity<PaymentCardDTO> activatePaymentCardById(@PathVariable Long paymentCardId) {
        PaymentCardDTO paymentCardDTO = paymentCardService.activatePaymentCardById(paymentCardId);
        return ResponseEntity.ok(paymentCardDTO);
    }

}
