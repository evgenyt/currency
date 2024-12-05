package com.tkachenya.currency.repository;

import com.tkachenya.currency.domain.Currency;
import com.tkachenya.currency.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    /**
     * Поиск курсов валют по базовой валюте и диапазону дат
     *
     * @param baseCurrency Базовая валюта
     * @param startDate Начальная дата диапазона
     * @param endDate Конечная дата диапазона
     * @return Список курсов валют относительно базовой валюты на указанный диапазон дат
     */
    @Query("""
        SELECT er FROM ExchangeRate er 
        JOIN FETCH er.quoteCurrency qc 
        WHERE er.baseCurrency = ?1 
        AND er.date BETWEEN ?3 AND ?2
        """)
    List<ExchangeRate> findByBaseCurrencyAndDateBetween(Currency baseCurrency, LocalDate startDate, LocalDate endDate);
}
