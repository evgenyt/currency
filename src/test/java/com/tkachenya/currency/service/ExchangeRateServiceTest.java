package com.tkachenya.currency.service;

import com.tkachenya.currency.domain.Currency;
import com.tkachenya.currency.domain.ExchangeRate;
import com.tkachenya.currency.dto.ExchangeRateDto;
import com.tkachenya.currency.mapper.ObjectMapper;
import com.tkachenya.currency.repository.CurrencyRepository;
import com.tkachenya.currency.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private Currency baseCurrency;
    private List<ExchangeRate> exchangeRates;
    private List<ExchangeRateDto> expectedDtos;

    @BeforeEach
    void setup() {
        var now = LocalDate.now();

        baseCurrency = new Currency();
        baseCurrency.id = 1L;
        baseCurrency.code = "USD";
        baseCurrency.description = "US Dollar";

        var currency1 = new Currency();
        currency1.code = "EUR";

        var exchangeRate1 = new ExchangeRate();
        exchangeRate1.id = 1L;
        exchangeRate1.date = now;
        exchangeRate1.quoteCurrency = currency1;
        exchangeRate1.rate = new BigDecimal("2");

        var currency2 = new Currency();
        currency2.code = "RUB";

        var exchangeRate2 = new ExchangeRate();
        exchangeRate2.id = 2L;
        exchangeRate2.date = now;
        exchangeRate2.quoteCurrency = currency2;
        exchangeRate2.rate = new BigDecimal("100");

        exchangeRates = List.of(exchangeRate1, exchangeRate2);

        expectedDtos = exchangeRates.stream()
                .map(exchangeRate -> {
                    var exchangeRateDto = new ExchangeRateDto();
                    exchangeRateDto.date = exchangeRate.date;
                    exchangeRateDto.code = exchangeRate.quoteCurrency.code;
                    exchangeRateDto.rate = exchangeRate.rate;

                    return exchangeRateDto;
                }).collect(Collectors.toList());
    }

    @Test
    void testGetExchangeRates() {
        var code = "USD";
        var startDate = LocalDate.now();
        var endDate = startDate.minusDays(1);

        when(currencyRepository.findByCode(code)).thenReturn(java.util.Optional.of(baseCurrency));
        when(exchangeRateRepository.findByBaseCurrencyAndDateBetween(any(), eq(startDate), eq(endDate))).thenReturn(exchangeRates);

        exchangeRates.forEach(exchangeRate -> {
                    var dto = new ExchangeRateDto();
                    dto.date = exchangeRate.date;
                    dto.code = exchangeRate.quoteCurrency.code;
                    dto.rate = exchangeRate.rate;

                    when(objectMapper.exchangeRateToExchangeRateDto(exchangeRate)).thenReturn(dto);
                }
        );

        List<ExchangeRateDto> result = exchangeRateService.getExchangeRates("USD", startDate, endDate);

        assertNotNull(result);
        assertEquals(expectedDtos.size(), result.size());

        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i).date, result.get(i).date);
            assertEquals(expectedDtos.get(i).code, result.get(i).code);
            assertEquals(expectedDtos.get(i).rate, result.get(i).rate);
        }
    }

    @Test
    void testGetExchangeRates_UnknownCurrency() {
        when(currencyRepository.findByCode(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getExchangeRates("XXX", LocalDate.now(), LocalDate.now().plusDays(1));
        });

        assertTrue(exception.getMessage().contains("Запрашиваемая валюта XXX не найдена"));
    }
}
