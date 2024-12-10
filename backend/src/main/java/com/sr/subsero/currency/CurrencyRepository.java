package com.sr.subsero.currency;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrencyRepository extends JpaRepository<Currency, String> {

    boolean existsByCodeIgnoreCase(String code);

}
