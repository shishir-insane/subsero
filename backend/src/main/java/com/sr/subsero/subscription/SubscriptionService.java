package com.sr.subsero.subscription;

import com.sr.subsero.currency.Currency;
import com.sr.subsero.currency.CurrencyRepository;
import com.sr.subsero.notification.Notification;
import com.sr.subsero.notification.NotificationRepository;
import com.sr.subsero.payment.Payment;
import com.sr.subsero.payment.PaymentRepository;
import com.sr.subsero.subscription_category.SubscriptionCategory;
import com.sr.subsero.subscription_category.SubscriptionCategoryRepository;
import com.sr.subsero.user.User;
import com.sr.subsero.user.UserRepository;
import com.sr.subsero.util.NotFoundException;
import com.sr.subsero.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final SubscriptionCategoryRepository subscriptionCategoryRepository;
    private final NotificationRepository notificationRepository;
    private final PaymentRepository paymentRepository;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
            final UserRepository userRepository, final CurrencyRepository currencyRepository,
            final SubscriptionCategoryRepository subscriptionCategoryRepository,
            final NotificationRepository notificationRepository,
            final PaymentRepository paymentRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.subscriptionCategoryRepository = subscriptionCategoryRepository;
        this.notificationRepository = notificationRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<SubscriptionDTO> findAll() {
        final List<Subscription> subscriptions = subscriptionRepository.findAll(Sort.by("id"));
        return subscriptions.stream()
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .toList();
    }

    public SubscriptionDTO get(final Long id) {
        return subscriptionRepository.findById(id)
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = new Subscription();
        mapToEntity(subscriptionDTO, subscription);
        return subscriptionRepository.save(subscription).getId();
    }

    public void update(final Long id, final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionDTO, subscription);
        subscriptionRepository.save(subscription);
    }

    public void delete(final Long id) {
        subscriptionRepository.deleteById(id);
    }

    private SubscriptionDTO mapToDTO(final Subscription subscription,
            final SubscriptionDTO subscriptionDTO) {
        subscriptionDTO.setId(subscription.getId());
        subscriptionDTO.setName(subscription.getName());
        subscriptionDTO.setDescription(subscription.getDescription());
        subscriptionDTO.setCost(subscription.getCost());
        subscriptionDTO.setRenewalDate(subscription.getRenewalDate());
        subscriptionDTO.setBillingCycle(subscription.getBillingCycle());
        subscriptionDTO.setPaymentMethod(subscription.getPaymentMethod());
        subscriptionDTO.setWebsiteUrl(subscription.getWebsiteUrl());
        subscriptionDTO.setIsActive(subscription.getIsActive());
        subscriptionDTO.setCreatedAt(subscription.getCreatedAt());
        subscriptionDTO.setUpdatedAt(subscription.getUpdatedAt());
        subscriptionDTO.setUser(subscription.getUser() == null ? null : subscription.getUser().getId());
        subscriptionDTO.setCurrencyCode(subscription.getCurrencyCode() == null ? null : subscription.getCurrencyCode().getCode());
        subscriptionDTO.setCategory(subscription.getCategory() == null ? null : subscription.getCategory().getId());
        return subscriptionDTO;
    }

    private Subscription mapToEntity(final SubscriptionDTO subscriptionDTO,
            final Subscription subscription) {
        subscription.setName(subscriptionDTO.getName());
        subscription.setDescription(subscriptionDTO.getDescription());
        subscription.setCost(subscriptionDTO.getCost());
        subscription.setRenewalDate(subscriptionDTO.getRenewalDate());
        subscription.setBillingCycle(subscriptionDTO.getBillingCycle());
        subscription.setPaymentMethod(subscriptionDTO.getPaymentMethod());
        subscription.setWebsiteUrl(subscriptionDTO.getWebsiteUrl());
        subscription.setIsActive(subscriptionDTO.getIsActive());
        subscription.setCreatedAt(subscriptionDTO.getCreatedAt());
        subscription.setUpdatedAt(subscriptionDTO.getUpdatedAt());
        final User user = subscriptionDTO.getUser() == null ? null : userRepository.findById(subscriptionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        subscription.setUser(user);
        final Currency currencyCode = subscriptionDTO.getCurrencyCode() == null ? null : currencyRepository.findById(subscriptionDTO.getCurrencyCode())
                .orElseThrow(() -> new NotFoundException("currencyCode not found"));
        subscription.setCurrencyCode(currencyCode);
        final SubscriptionCategory category = subscriptionDTO.getCategory() == null ? null : subscriptionCategoryRepository.findById(subscriptionDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        subscription.setCategory(category);
        return subscription;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Notification subscriptionNotification = notificationRepository.findFirstBySubscription(subscription);
        if (subscriptionNotification != null) {
            referencedWarning.setKey("subscription.notification.subscription.referenced");
            referencedWarning.addParam(subscriptionNotification.getId());
            return referencedWarning;
        }
        final Payment subscriptionPayment = paymentRepository.findFirstBySubscription(subscription);
        if (subscriptionPayment != null) {
            referencedWarning.setKey("subscription.payment.subscription.referenced");
            referencedWarning.addParam(subscriptionPayment.getId());
            return referencedWarning;
        }
        return null;
    }

}
