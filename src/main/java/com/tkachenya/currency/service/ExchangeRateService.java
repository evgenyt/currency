package com.tkachenya.currency.service;

import com.tkachenya.currency.domain.Currency;
import com.tkachenya.currency.dto.ExchangeRateDto;
import com.tkachenya.currency.mapper.ObjectMapper;
import com.tkachenya.currency.repository.CurrencyRepository;
import com.tkachenya.currency.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ExchangeRateService {
    private final Logger logger = getLogger(ExchangeRateService.class);

    private final ObjectMapper objectMapper;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(ObjectMapper objectMapper, CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository) {
        this.objectMapper = objectMapper;
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * Получение списка курсов валют по ISO-коду базовой валюты на указанный диапазон дат
     *
     * @param baseCurrencyIsoCode ISO-код базовой валюты
     * @param startDate Начальная дата диапазона
     * @param endDate Конечная дата диапазона
     * @return Список курсов валют относительно базовой валюты на указанный диапазон дат
     */
    public List<ExchangeRateDto> getExchangeRates(String baseCurrencyIsoCode, LocalDate startDate, LocalDate endDate) {
        Currency baseCurrency = currencyRepository.findByCode(baseCurrencyIsoCode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Запрашиваемая валюта " + baseCurrencyIsoCode + " не найдена"));

        return exchangeRateRepository.findByBaseCurrencyAndDateBetween(baseCurrency, startDate, endDate)
                .stream().map(objectMapper::exchangeRateToExchangeRateDto).collect(toList());
    }
}
