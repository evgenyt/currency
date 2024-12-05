package com.tkachenya.currency.service;

import com.tkachenya.currency.dto.CurrencyDto;
import com.tkachenya.currency.mapper.ObjectMapper;
import com.tkachenya.currency.repository.CurrencyRepository;
import com.tkachenya.currency.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CurrencyService {
    private final Logger logger = getLogger(CurrencyService.class);

    private final ObjectMapper objectMapper;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(ObjectMapper objectMapper, CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository) {
        this.objectMapper = objectMapper;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Получение списка валют
     *
     * @return Список валют
     */
    public List<CurrencyDto> getCurrencies() {
        return currencyRepository.findAll().stream().map(objectMapper::currencyToCurrencyDto).collect(toList());
    }
}
