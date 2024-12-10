package com.sr.subsero.analytic;

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
@RequestMapping(value = "/api/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnalyticResource {

    private final AnalyticService analyticService;

    public AnalyticResource(final AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping
    public ResponseEntity<List<AnalyticDTO>> getAllAnalytics() {
        return ResponseEntity.ok(analyticService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticDTO> getAnalytic(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(analyticService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAnalytic(
            @RequestBody @Valid final AnalyticDTO analyticDTO) {
        final Long createdId = analyticService.create(analyticDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAnalytic(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AnalyticDTO analyticDTO) {
        analyticService.update(id, analyticDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAnalytic(@PathVariable(name = "id") final Long id) {
        analyticService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
