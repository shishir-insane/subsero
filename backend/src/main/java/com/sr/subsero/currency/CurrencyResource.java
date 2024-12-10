package com.sr.subsero.currency;

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
@RequestMapping(value = "/api/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyResource {

    private final CurrencyService currencyService;

    public CurrencyResource(final CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable(name = "code") final String code) {
        return ResponseEntity.ok(currencyService.get(code));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createCurrency(
            @RequestBody @Valid final CurrencyDTO currencyDTO) {
        final String createdCode = currencyService.create(currencyDTO);
        return new ResponseEntity<>('"' + createdCode + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<String> updateCurrency(@PathVariable(name = "code") final String code,
            @RequestBody @Valid final CurrencyDTO currencyDTO) {
        currencyService.update(code, currencyDTO);
        return ResponseEntity.ok('"' + code + '"');
    }

    @DeleteMapping("/{code}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCurrency(@PathVariable(name = "code") final String code) {
        final ReferencedWarning referencedWarning = currencyService.getReferencedWarning(code);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        currencyService.delete(code);
        return ResponseEntity.noContent().build();
    }

}
