package com.tkachenya.currency.repository;

import com.tkachenya.currency.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Поиск валюты по ее ISO-коду
     *
     * @param code ISO-код валюты
     * @return Найденная валюта, если она существует
     */
    Optional<Currency> findByCode(String code);
}
