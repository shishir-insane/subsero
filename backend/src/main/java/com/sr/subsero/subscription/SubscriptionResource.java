package com.sr.subsero.subscription;

import com.sr.subsero.util.ReferencedException;
import com.sr.subsero.util.ReferencedWarning;
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
@RequestMapping(value = "/api/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionResource {

    private final SubscriptionService subscriptionService;

    public SubscriptionResource(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscription(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(subscriptionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSubscription(
            @RequestBody @Valid final SubscriptionDTO subscriptionDTO) {
        final Long createdId = subscriptionService.create(subscriptionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSubscription(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SubscriptionDTO subscriptionDTO) {
        subscriptionService.update(id, subscriptionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSubscription(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = subscriptionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
