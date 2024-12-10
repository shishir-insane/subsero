package com.sr.subsero.user_subscription_analytic;

import com.sr.subsero.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSubscriptionAnalyticRepository extends JpaRepository<UserSubscriptionAnalytic, Long> {

    UserSubscriptionAnalytic findFirstByUser(User user);

}
