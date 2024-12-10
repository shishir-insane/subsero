package com.sr.subsero.notification;

import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findFirstByUser(User user);

    Notification findFirstBySubscription(Subscription subscription);

}
