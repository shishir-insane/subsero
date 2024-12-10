package com.sr.subsero.user_subscription_analytic;

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
@RequestMapping(value = "/api/userSubscriptionAnalytics", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserSubscriptionAnalyticResource {

    private final UserSubscriptionAnalyticService userSubscriptionAnalyticService;

    public UserSubscriptionAnalyticResource(
            final UserSubscriptionAnalyticService userSubscriptionAnalyticService) {
        this.userSubscriptionAnalyticService = userSubscriptionAnalyticService;
    }

    @GetMapping
    public ResponseEntity<List<UserSubscriptionAnalyticDTO>> getAllUserSubscriptionAnalytics() {
        return ResponseEntity.ok(userSubscriptionAnalyticService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionAnalyticDTO> getUserSubscriptionAnalytic(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(userSubscriptionAnalyticService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUserSubscriptionAnalytic(
            @RequestBody @Valid final UserSubscriptionAnalyticDTO userSubscriptionAnalyticDTO) {
        final Long createdId = userSubscriptionAnalyticService.create(userSubscriptionAnalyticDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUserSubscriptionAnalytic(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UserSubscriptionAnalyticDTO userSubscriptionAnalyticDTO) {
        userSubscriptionAnalyticService.update(id, userSubscriptionAnalyticDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserSubscriptionAnalytic(
            @PathVariable(name = "id") final Long id) {
        userSubscriptionAnalyticService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
