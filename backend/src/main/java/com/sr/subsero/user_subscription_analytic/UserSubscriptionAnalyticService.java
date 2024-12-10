package com.sr.subsero.user_subscription_analytic;

import com.sr.subsero.user.User;
import com.sr.subsero.user.UserRepository;
import com.sr.subsero.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserSubscriptionAnalyticService {

    private final UserSubscriptionAnalyticRepository userSubscriptionAnalyticRepository;
    private final UserRepository userRepository;

    public UserSubscriptionAnalyticService(
            final UserSubscriptionAnalyticRepository userSubscriptionAnalyticRepository,
            final UserRepository userRepository) {
        this.userSubscriptionAnalyticRepository = userSubscriptionAnalyticRepository;
        this.userRepository = userRepository;
    }

    public List<UserSubscriptionAnalyticDTO> findAll() {
        final List<UserSubscriptionAnalytic> userSubscriptionAnalytics = userSubscriptionAnalyticRepository.findAll(Sort.by("id"));
        return userSubscriptionAnalytics.stream()
                .map(userSubscriptionAnalytic -> mapToDTO(userSubscriptionAnalytic, new UserSubscriptionAnalyticDTO()))
                .toList();
    }

    public UserSubscriptionAnalyticDTO get(final Long id) {
        return userSubscriptionAnalyticRepository.findById(id)
                .map(userSubscriptionAnalytic -> mapToDTO(userSubscriptionAnalytic, new UserSubscriptionAnalyticDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserSubscriptionAnalyticDTO userSubscriptionAnalyticDTO) {
        final UserSubscriptionAnalytic userSubscriptionAnalytic = new UserSubscriptionAnalytic();
        mapToEntity(userSubscriptionAnalyticDTO, userSubscriptionAnalytic);
        return userSubscriptionAnalyticRepository.save(userSubscriptionAnalytic).getId();
    }

    public void update(final Long id,
            final UserSubscriptionAnalyticDTO userSubscriptionAnalyticDTO) {
        final UserSubscriptionAnalytic userSubscriptionAnalytic = userSubscriptionAnalyticRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userSubscriptionAnalyticDTO, userSubscriptionAnalytic);
        userSubscriptionAnalyticRepository.save(userSubscriptionAnalytic);
    }

    public void delete(final Long id) {
        userSubscriptionAnalyticRepository.deleteById(id);
    }

    private UserSubscriptionAnalyticDTO mapToDTO(
            final UserSubscriptionAnalytic userSubscriptionAnalytic,
            final UserSubscriptionAnalyticDTO userSubscriptionAnalyticDTO) {
        userSubscriptionAnalyticDTO.setId(userSubscriptionAnalytic.getId());
        userSubscriptionAnalyticDTO.setTotalMonthlyCost(userSubscriptionAnalytic.getTotalMonthlyCost());
        userSubscriptionAnalyticDTO.setTotalAnnualCost(userSubscriptionAnalytic.getTotalAnnualCost());
        userSubscriptionAnalyticDTO.setMostExpensiveSubscription(userSubscriptionAnalytic.getMostExpensiveSubscription());
        userSubscriptionAnalyticDTO.setLeastUsedSubscription(userSubscriptionAnalytic.getLeastUsedSubscription());
        userSubscriptionAnalyticDTO.setTotalSubscriptions(userSubscriptionAnalytic.getTotalSubscriptions());
        userSubscriptionAnalyticDTO.setActiveSubscriptions(userSubscriptionAnalytic.getActiveSubscriptions());
        userSubscriptionAnalyticDTO.setPotentialSavings(userSubscriptionAnalytic.getPotentialSavings());
        userSubscriptionAnalyticDTO.setUpdatedAt(userSubscriptionAnalytic.getUpdatedAt());
        userSubscriptionAnalyticDTO.setUser(userSubscriptionAnalytic.getUser() == null ? null : userSubscriptionAnalytic.getUser().getId());
        return userSubscriptionAnalyticDTO;
    }

    private UserSubscriptionAnalytic mapToEntity(
            final UserSubscriptionAnalyticDTO userSubscriptionAnalyticDTO,
            final UserSubscriptionAnalytic userSubscriptionAnalytic) {
        userSubscriptionAnalytic.setTotalMonthlyCost(userSubscriptionAnalyticDTO.getTotalMonthlyCost());
        userSubscriptionAnalytic.setTotalAnnualCost(userSubscriptionAnalyticDTO.getTotalAnnualCost());
        userSubscriptionAnalytic.setMostExpensiveSubscription(userSubscriptionAnalyticDTO.getMostExpensiveSubscription());
        userSubscriptionAnalytic.setLeastUsedSubscription(userSubscriptionAnalyticDTO.getLeastUsedSubscription());
        userSubscriptionAnalytic.setTotalSubscriptions(userSubscriptionAnalyticDTO.getTotalSubscriptions());
        userSubscriptionAnalytic.setActiveSubscriptions(userSubscriptionAnalyticDTO.getActiveSubscriptions());
        userSubscriptionAnalytic.setPotentialSavings(userSubscriptionAnalyticDTO.getPotentialSavings());
        userSubscriptionAnalytic.setUpdatedAt(userSubscriptionAnalyticDTO.getUpdatedAt());
        final User user = userSubscriptionAnalyticDTO.getUser() == null ? null : userRepository.findById(userSubscriptionAnalyticDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        userSubscriptionAnalytic.setUser(user);
        return userSubscriptionAnalytic;
    }

}
