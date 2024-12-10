package com.sr.subsero.user;

import com.sr.subsero.currency.Currency;
import com.sr.subsero.currency.CurrencyRepository;
import com.sr.subsero.notification.Notification;
import com.sr.subsero.notification.NotificationRepository;
import com.sr.subsero.payment.Payment;
import com.sr.subsero.payment.PaymentRepository;
import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.subscription.SubscriptionRepository;
import com.sr.subsero.user_subscription_analytic.UserSubscriptionAnalytic;
import com.sr.subsero.user_subscription_analytic.UserSubscriptionAnalyticRepository;
import com.sr.subsero.util.NotFoundException;
import com.sr.subsero.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final UserSubscriptionAnalyticRepository userSubscriptionAnalyticRepository;
    private final PaymentRepository paymentRepository;

    public UserService(final UserRepository userRepository,
            final CurrencyRepository currencyRepository,
            final SubscriptionRepository subscriptionRepository,
            final NotificationRepository notificationRepository,
            final UserSubscriptionAnalyticRepository userSubscriptionAnalyticRepository,
            final PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationRepository = notificationRepository;
        this.userSubscriptionAnalyticRepository = userSubscriptionAnalyticRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPasswordHash(user.getPasswordHash());
        userDTO.setTimezone(user.getTimezone());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setLastLogin(user.getLastLogin());
        userDTO.setPreferredCurrency(user.getPreferredCurrency() == null ? null : user.getPreferredCurrency().getCode());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(userDTO.getPasswordHash());
        user.setTimezone(userDTO.getTimezone());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        user.setLastLogin(userDTO.getLastLogin());
        final Currency preferredCurrency = userDTO.getPreferredCurrency() == null ? null : currencyRepository.findById(userDTO.getPreferredCurrency())
                .orElseThrow(() -> new NotFoundException("preferredCurrency not found"));
        user.setPreferredCurrency(preferredCurrency);
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Subscription userSubscription = subscriptionRepository.findFirstByUser(user);
        if (userSubscription != null) {
            referencedWarning.setKey("user.subscription.user.referenced");
            referencedWarning.addParam(userSubscription.getId());
            return referencedWarning;
        }
        final Notification userNotification = notificationRepository.findFirstByUser(user);
        if (userNotification != null) {
            referencedWarning.setKey("user.notification.user.referenced");
            referencedWarning.addParam(userNotification.getId());
            return referencedWarning;
        }
        final UserSubscriptionAnalytic userUserSubscriptionAnalytic = userSubscriptionAnalyticRepository.findFirstByUser(user);
        if (userUserSubscriptionAnalytic != null) {
            referencedWarning.setKey("user.userSubscriptionAnalytic.user.referenced");
            referencedWarning.addParam(userUserSubscriptionAnalytic.getId());
            return referencedWarning;
        }
        final Payment userPayment = paymentRepository.findFirstByUser(user);
        if (userPayment != null) {
            referencedWarning.setKey("user.payment.user.referenced");
            referencedWarning.addParam(userPayment.getId());
            return referencedWarning;
        }
        return null;
    }

}
