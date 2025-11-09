package UserService.controller;

import UserService.DTO.PaymentCardDTO;
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
    public ResponseEntity<PaymentCardDTO> getPaymentCardById(@PathVariable UUID paymentCardId) {
        PaymentCardDTO paymentCard = paymentCardService.getPaymentCardById(paymentCardId);
        return ResponseEntity.ok(paymentCard);
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<PaymentCardDTO>> getAllPaymentCardsFilteredBy(
            @Valid PaymentCardFilterRequest paymentCardFilterRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PaymentCardDTO> paymentCardDTOS = paymentCardService.getAllPaymentCardsFilteredBy(paymentCardFilterRequest, pageable);
        return ResponseEntity.ok(paymentCardDTOS);
    }

    @PutMapping("/{paymentCardId}")
    public ResponseEntity<PaymentCardDTO> updatePaymentCardById(
            @PathVariable UUID paymentCardId,
            @Valid @RequestBody PaymentCardDTO paymentCardDTO) {

        PaymentCardDTO updatedPaymentCard = paymentCardService.updatePaymentCardById(paymentCardId, paymentCardDTO);
        return ResponseEntity.ok(updatedPaymentCard);
    }

    @PatchMapping("/{paymentCardId}/deactivate")
    public ResponseEntity<Void> deactivatePaymentCardById(@PathVariable UUID paymentCardId) {
        paymentCardService.deactivatePaymentCardById(paymentCardId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{paymentCardId}/activate")
    public ResponseEntity<Void> activatePaymentCardById(@PathVariable UUID paymentCardId) {
        paymentCardService.activatePaymentCardById(paymentCardId);
        return ResponseEntity.noContent().build();
    }

}
