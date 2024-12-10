package com.sr.subsero.currency;

import com.sr.subsero.payment.Payment;
import com.sr.subsero.payment.PaymentRepository;
import com.sr.subsero.subscription.Subscription;
import com.sr.subsero.subscription.SubscriptionRepository;
import com.sr.subsero.user.User;
import com.sr.subsero.user.UserRepository;
import com.sr.subsero.util.NotFoundException;
import com.sr.subsero.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    public CurrencyService(final CurrencyRepository currencyRepository,
            final UserRepository userRepository,
            final SubscriptionRepository subscriptionRepository,
            final PaymentRepository paymentRepository) {
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<CurrencyDTO> findAll() {
        final List<Currency> currencies = currencyRepository.findAll(Sort.by("code"));
        return currencies.stream()
                .map(currency -> mapToDTO(currency, new CurrencyDTO()))
                .toList();
    }

    public CurrencyDTO get(final String code) {
        return currencyRepository.findById(code)
                .map(currency -> mapToDTO(currency, new CurrencyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final CurrencyDTO currencyDTO) {
        final Currency currency = new Currency();
        mapToEntity(currencyDTO, currency);
        currency.setCode(currencyDTO.getCode());
        return currencyRepository.save(currency).getCode();
    }

    public void update(final String code, final CurrencyDTO currencyDTO) {
        final Currency currency = currencyRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        mapToEntity(currencyDTO, currency);
        currencyRepository.save(currency);
    }

    public void delete(final String code) {
        currencyRepository.deleteById(code);
    }

    private CurrencyDTO mapToDTO(final Currency currency, final CurrencyDTO currencyDTO) {
        currencyDTO.setCode(currency.getCode());
        currencyDTO.setName(currency.getName());
        currencyDTO.setSymbol(currency.getSymbol());
        currencyDTO.setExchangeRate(currency.getExchangeRate());
        currencyDTO.setUpdatedAt(currency.getUpdatedAt());
        return currencyDTO;
    }

    private Currency mapToEntity(final CurrencyDTO currencyDTO, final Currency currency) {
        currency.setName(currencyDTO.getName());
        currency.setSymbol(currencyDTO.getSymbol());
        currency.setExchangeRate(currencyDTO.getExchangeRate());
        currency.setUpdatedAt(currencyDTO.getUpdatedAt());
        return currency;
    }

    public boolean codeExists(final String code) {
        return currencyRepository.existsByCodeIgnoreCase(code);
    }

    public ReferencedWarning getReferencedWarning(final String code) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Currency currency = currencyRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        final User preferredCurrencyUser = userRepository.findFirstByPreferredCurrency(currency);
        if (preferredCurrencyUser != null) {
            referencedWarning.setKey("currency.user.preferredCurrency.referenced");
            referencedWarning.addParam(preferredCurrencyUser.getId());
            return referencedWarning;
        }
        final Subscription currencyCodeSubscription = subscriptionRepository.findFirstByCurrencyCode(currency);
        if (currencyCodeSubscription != null) {
            referencedWarning.setKey("currency.subscription.currencyCode.referenced");
            referencedWarning.addParam(currencyCodeSubscription.getId());
            return referencedWarning;
        }
        final Payment currencyCodePayment = paymentRepository.findFirstByCurrencyCode(currency);
        if (currencyCodePayment != null) {
            referencedWarning.setKey("currency.payment.currencyCode.referenced");
            referencedWarning.addParam(currencyCodePayment.getId());
            return referencedWarning;
        }
        return null;
    }

}
