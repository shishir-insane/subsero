package com.sr.subsero.subscription_category;

import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.subscription.SubscriptionRepository;
import com.sr.subsero.util.NotFoundException;
import com.sr.subsero.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionCategoryService {

    private final SubscriptionCategoryRepository subscriptionCategoryRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionCategoryService(
            final SubscriptionCategoryRepository subscriptionCategoryRepository,
            final SubscriptionRepository subscriptionRepository) {
        this.subscriptionCategoryRepository = subscriptionCategoryRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionCategoryDTO> findAll() {
        final List<SubscriptionCategory> subscriptionCategories = subscriptionCategoryRepository.findAll(Sort.by("id"));
        return subscriptionCategories.stream()
                .map(subscriptionCategory -> mapToDTO(subscriptionCategory, new SubscriptionCategoryDTO()))
                .toList();
    }

    public SubscriptionCategoryDTO get(final Long id) {
        return subscriptionCategoryRepository.findById(id)
                .map(subscriptionCategory -> mapToDTO(subscriptionCategory, new SubscriptionCategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SubscriptionCategoryDTO subscriptionCategoryDTO) {
        final SubscriptionCategory subscriptionCategory = new SubscriptionCategory();
        mapToEntity(subscriptionCategoryDTO, subscriptionCategory);
        return subscriptionCategoryRepository.save(subscriptionCategory).getId();
    }

    public void update(final Long id, final SubscriptionCategoryDTO subscriptionCategoryDTO) {
        final SubscriptionCategory subscriptionCategory = subscriptionCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionCategoryDTO, subscriptionCategory);
        subscriptionCategoryRepository.save(subscriptionCategory);
    }

    public void delete(final Long id) {
        subscriptionCategoryRepository.deleteById(id);
    }

    private SubscriptionCategoryDTO mapToDTO(final SubscriptionCategory subscriptionCategory,
            final SubscriptionCategoryDTO subscriptionCategoryDTO) {
        subscriptionCategoryDTO.setId(subscriptionCategory.getId());
        subscriptionCategoryDTO.setName(subscriptionCategory.getName());
        subscriptionCategoryDTO.setDescription(subscriptionCategory.getDescription());
        subscriptionCategoryDTO.setIcon(subscriptionCategory.getIcon());
        return subscriptionCategoryDTO;
    }

    private SubscriptionCategory mapToEntity(final SubscriptionCategoryDTO subscriptionCategoryDTO,
            final SubscriptionCategory subscriptionCategory) {
        subscriptionCategory.setName(subscriptionCategoryDTO.getName());
        subscriptionCategory.setDescription(subscriptionCategoryDTO.getDescription());
        subscriptionCategory.setIcon(subscriptionCategoryDTO.getIcon());
        return subscriptionCategory;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final SubscriptionCategory subscriptionCategory = subscriptionCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Subscription categorySubscription = subscriptionRepository.findFirstByCategory(subscriptionCategory);
        if (categorySubscription != null) {
            referencedWarning.setKey("subscriptionCategory.subscription.category.referenced");
            referencedWarning.addParam(categorySubscription.getId());
            return referencedWarning;
        }
        return null;
    }

}
