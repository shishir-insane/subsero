package com.sr.subsero.subscription_category;

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
@RequestMapping(value = "/api/subscriptionCategories", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionCategoryResource {

    private final SubscriptionCategoryService subscriptionCategoryService;

    public SubscriptionCategoryResource(
            final SubscriptionCategoryService subscriptionCategoryService) {
        this.subscriptionCategoryService = subscriptionCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionCategoryDTO>> getAllSubscriptionCategories() {
        return ResponseEntity.ok(subscriptionCategoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionCategoryDTO> getSubscriptionCategory(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(subscriptionCategoryService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSubscriptionCategory(
            @RequestBody @Valid final SubscriptionCategoryDTO subscriptionCategoryDTO) {
        final Long createdId = subscriptionCategoryService.create(subscriptionCategoryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSubscriptionCategory(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SubscriptionCategoryDTO subscriptionCategoryDTO) {
        subscriptionCategoryService.update(id, subscriptionCategoryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSubscriptionCategory(
            @PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = subscriptionCategoryService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        subscriptionCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
