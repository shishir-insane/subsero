package com.sr.subsero.currency;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CurrencyDTO {

    @Size(max = 3)
    @CurrencyCodeValid
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 10)
    private String symbol;

    @NotNull
    @Digits(integer = 14, fraction = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "19.0008")
    private BigDecimal exchangeRate;

    private OffsetDateTime updatedAt;

}
