package com.tkachenya.currency.controller.rest.impl;

import com.tkachenya.currency.controller.rest.CurrencyController;
import com.tkachenya.currency.dto.CurrencyDto;
import com.tkachenya.currency.service.CurrencyService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class CurrencyControllerImpl implements CurrencyController {
    private final Logger logger = getLogger(CurrencyControllerImpl.class);

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyControllerImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * Получение списка валют
     *
     * @return Список валют
     */
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
        List<CurrencyDto> currencies = currencyService.getCurrencies();

        return currencies.isEmpty()
                ? new ResponseEntity<>(null, NO_CONTENT)
                : new ResponseEntity<>(currencies, OK);
    }
}
