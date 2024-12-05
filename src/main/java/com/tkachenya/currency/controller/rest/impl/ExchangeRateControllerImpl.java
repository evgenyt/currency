package com.tkachenya.currency.controller.rest.impl;

import com.tkachenya.currency.controller.rest.ExchangeRateController;
import com.tkachenya.currency.dto.ExchangeRateDto;
import com.tkachenya.currency.service.ExchangeRateService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ExchangeRateControllerImpl implements ExchangeRateController {
    private final Logger logger = getLogger(ExchangeRateControllerImpl.class);

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateControllerImpl(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Получение списка курсов валют по ISO-коду базовой валюты на текущий день и предыдущие дни
     *
     * @param code ISO-код базовой валюты
     * @param daysCount Количество предыдущих дней
     * @return Список курсов валют относительно базовой валюты на указанные даты
     */
    public ResponseEntity<List<ExchangeRateDto>> getExchangeRates(String code, int daysCount) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(daysCount);

        List<ExchangeRateDto> exchangeRates = exchangeRateService.getExchangeRates(code, startDate, endDate);

        return exchangeRates.isEmpty()
                ? new ResponseEntity<>(null, NO_CONTENT)
                : new ResponseEntity<>(exchangeRates, OK);
    }
}
