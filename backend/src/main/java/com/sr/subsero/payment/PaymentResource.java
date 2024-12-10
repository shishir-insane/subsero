package com.sr.subsero.payment;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentResource {

    private final PaymentService paymentService;

    public PaymentResource(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(paymentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPayment(@RequestBody @Valid final PaymentDTO paymentDTO) {
        final Long createdId = paymentService.create(paymentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePayment(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PaymentDTO paymentDTO) {
        paymentService.update(id, paymentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePayment(@PathVariable(name = "id") final Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
