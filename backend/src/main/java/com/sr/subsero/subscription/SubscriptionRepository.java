package com.sr.subsero.subscription;

import com.sr.subsero.currency.Currency;
import com.sr.subsero.subscription_category.SubscriptionCategory;
import com.sr.subsero.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findFirstByUser(User user);

    Subscription findFirstByCurrencyCode(Currency currency);

    Subscription findFirstByCategory(SubscriptionCategory subscriptionCategory);

}
