package com.sr.subsero.user_subscription_analytic;

import com.sr.subsero.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "UserSubscriptionAnalytics")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UserSubscriptionAnalytic {

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

    @Column
    private Long totalSubscriptions;

    @Column
    private Long activeSubscriptions;

    @Column(precision = 12, scale = 2)
    private BigDecimal potentialSavings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

}
