package com.sr.subsero.payment;

import com.sr.subsero.currency.Currency;
import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findFirstByUser(User user);

    Payment findFirstBySubscription(Subscription subscription);

    Payment findFirstByCurrencyCode(Currency currency);

}
