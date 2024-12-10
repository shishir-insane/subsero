package com.sr.subsero.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubscriptionDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "65.08")
    private BigDecimal cost;

    @NotNull
    private LocalDate renewalDate;

    @NotNull
    @Size(max = 255)
    private String billingCycle;

    @Size(max = 50)
    private String paymentMethod;

    @Size(max = 255)
    private String websiteUrl;

    @JsonProperty("isActive")
    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @NotNull
    private Long user;

    @NotNull
    @Size(max = 3)
    private String currencyCode;

    private Long category;

}
