package com.sr.subsero.subscription_category;

import com.sr.subsero.subscription.Subscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "SubscriptionCategories")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class SubscriptionCategory {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "\"description\"", columnDefinition = "longtext")
    private String description;

    @Column(length = 100)
    private String icon;

    @OneToMany(mappedBy = "category")
    private Set<Subscription> categorySubscriptions;
}
