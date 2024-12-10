package com.sr.subsero.payment;

import com.sr.subsero.currency.Currency;
import com.sr.subsero.currency.CurrencyRepository;
import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.subscription.SubscriptionRepository;
import com.sr.subsero.user.User;
import com.sr.subsero.user.UserRepository;
import com.sr.subsero.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CurrencyRepository currencyRepository;

    public PaymentService(final PaymentRepository paymentRepository,
            final UserRepository userRepository,
            final SubscriptionRepository subscriptionRepository,
            final CurrencyRepository currencyRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final Long id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getId();
    }

    public void update(final Long id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final Long id) {
        paymentRepository.deleteById(id);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setCreatedAt(payment.getCreatedAt());
        paymentDTO.setUser(payment.getUser() == null ? null : payment.getUser().getId());
        paymentDTO.setSubscription(payment.getSubscription() == null ? null : payment.getSubscription().getId());
        paymentDTO.setCurrencyCode(payment.getCurrencyCode() == null ? null : payment.getCurrencyCode().getCode());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setStatus(paymentDTO.getStatus());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setCreatedAt(paymentDTO.getCreatedAt());
        final User user = paymentDTO.getUser() == null ? null : userRepository.findById(paymentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        payment.setUser(user);
        final Subscription subscription = paymentDTO.getSubscription() == null ? null : subscriptionRepository.findById(paymentDTO.getSubscription())
                .orElseThrow(() -> new NotFoundException("subscription not found"));
        payment.setSubscription(subscription);
        final Currency currencyCode = paymentDTO.getCurrencyCode() == null ? null : currencyRepository.findById(paymentDTO.getCurrencyCode())
                .orElseThrow(() -> new NotFoundException("currencyCode not found"));
        payment.setCurrencyCode(currencyCode);
        return payment;
    }

}
