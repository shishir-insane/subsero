package com.sr.subsero.currency;

import com.sr.subsero.payment.Payment;
import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Currencies")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Currency {

    @Id
    @Column(nullable = false, updatable = false, length = 3)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal exchangeRate;

    @OneToMany(mappedBy = "preferredCurrency")
    private Set<User> preferredCurrencyUsers;

    @OneToMany(mappedBy = "currencyCode")
    private Set<Subscription> currencyCodeSubscriptions;

    @OneToMany(mappedBy = "currencyCode")
    private Set<Payment> currencyCodePayments;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

}
