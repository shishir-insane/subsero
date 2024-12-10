package com.sr.subsero.subscription;

import com.sr.subsero.currency.Currency;
import com.sr.subsero.notification.Notification;
import com.sr.subsero.payment.Payment;
import com.sr.subsero.subscription_category.SubscriptionCategory;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Subscriptions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Subscription {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "\"description\"", columnDefinition = "longtext")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    private LocalDate renewalDate;

    @Column(nullable = false)
    private String billingCycle;

    @Column(length = 50)
    private String paymentMethod;

    @Column
    private String websiteUrl;

    @Column(columnDefinition = "tinyint", length = 1)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code_id", nullable = false)
    private Currency currencyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SubscriptionCategory category;

    @OneToMany(mappedBy = "subscription")
    private Set<Notification> subscriptionNotifications;

    @OneToMany(mappedBy = "subscription")
    private Set<Payment> subscriptionPayments;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

}
