package com.sr.subsero.analytic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Analytics")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Analytic {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalMonthlyCost;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAnnualCost;

    @Column(length = 100)
    private String mostExpensiveSubscription;

    @Column(length = 100)
    private String leastUsedSubscription;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime updatedAt;
}
