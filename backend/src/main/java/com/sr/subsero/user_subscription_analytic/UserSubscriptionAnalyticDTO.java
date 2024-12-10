package com.sr.subsero.user_subscription_analytic;

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
public class UserSubscriptionAnalyticDTO {

    private Long id;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "26.08")
    private BigDecimal totalMonthlyCost;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "40.08")
    private BigDecimal totalAnnualCost;

    @Size(max = 100)
    private String mostExpensiveSubscription;

    @Size(max = 100)
    private String leastUsedSubscription;

    private Long totalSubscriptions;

    private Long activeSubscriptions;

    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "13.08")
    private BigDecimal potentialSavings;

    private OffsetDateTime updatedAt;

    @NotNull
    private Long user;

}
