package com.sr.subsero.user;

import com.sr.subsero.currency.Currency;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByPreferredCurrency(Currency currency);

    Optional<User> findByEmail(String email);

}
