package com.sr.subsero.notification;

import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.subscription.SubscriptionRepository;
import com.sr.subsero.user.User;
import com.sr.subsero.user.UserRepository;
import com.sr.subsero.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public NotificationService(final NotificationRepository notificationRepository,
            final UserRepository userRepository,
            final SubscriptionRepository subscriptionRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<NotificationDTO> findAll() {
        final List<Notification> notifications = notificationRepository.findAll(Sort.by("id"));
        return notifications.stream()
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .toList();
    }

    public NotificationDTO get(final Long id) {
        return notificationRepository.findById(id)
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getId();
    }

    public void update(final Long id, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(final Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO mapToDTO(final Notification notification,
            final NotificationDTO notificationDTO) {
        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setMethod(notification.getMethod());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setReminderDate(notification.getReminderDate());
        notificationDTO.setStatus(notification.getStatus());
        notificationDTO.setPriority(notification.getPriority());
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        notificationDTO.setUser(notification.getUser() == null ? null : notification.getUser().getId());
        notificationDTO.setSubscription(notification.getSubscription() == null ? null : notification.getSubscription().getId());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
            final Notification notification) {
        notification.setType(notificationDTO.getType());
        notification.setMethod(notificationDTO.getMethod());
        notification.setMessage(notificationDTO.getMessage());
        notification.setReminderDate(notificationDTO.getReminderDate());
        notification.setStatus(notificationDTO.getStatus());
        notification.setPriority(notificationDTO.getPriority());
        notification.setCreatedAt(notificationDTO.getCreatedAt());
        final User user = notificationDTO.getUser() == null ? null : userRepository.findById(notificationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        notification.setUser(user);
        final Subscription subscription = notificationDTO.getSubscription() == null ? null : subscriptionRepository.findById(notificationDTO.getSubscription())
                .orElseThrow(() -> new NotFoundException("subscription not found"));
        notification.setSubscription(subscription);
        return notification;
    }

}
