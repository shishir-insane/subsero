package com.sr.subsero.payment;

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
public class PaymentDTO {

    private Long id;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "92.08")
    private BigDecimal amount;

    @NotNull
    private OffsetDateTime paymentDate;

    @NotNull
    @Size(max = 255)
    private String status;

    @Size(max = 50)
    private String paymentMethod;

    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

    @NotNull
    private Long subscription;

    @NotNull
    @Size(max = 3)
    private String currencyCode;

}
