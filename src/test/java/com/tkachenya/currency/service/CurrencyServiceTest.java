package com.tkachenya.currency.service;

import com.tkachenya.currency.domain.Currency;
import com.tkachenya.currency.dto.CurrencyDto;
import com.tkachenya.currency.mapper.ObjectMapper;
import com.tkachenya.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private List<Currency> currencies;
    private List<CurrencyDto> expectedDtos;

    @BeforeEach
    void setUp() {
        var currency1 = new Currency();
        currency1.id = 1L;
        currency1.code = "USD";
        currency1.description = "US Dollar";

        var currency2 = new Currency();
        currency2.id = 2L;
        currency2.code = "EUR";
        currency2.description = "Euro";

        currencies = List.of(currency1, currency2);

        expectedDtos = currencies.stream()
                .map(currency -> {
                    var currencyDto = new CurrencyDto();
                    currencyDto.code = currency.code;
                    currencyDto.description = currency.description;

                    return currencyDto;
                }).collect(Collectors.toList());
    }

    @Test
    void testGetCurrencies() {
        when(currencyRepository.findAll()).thenReturn(currencies);

        currencies.forEach(currency -> {
            var dto = new CurrencyDto();
            dto.code = currency.code;
            dto.description = currency.description;

            when(objectMapper.currencyToCurrencyDto(currency)).thenReturn(dto);
        });

        List<CurrencyDto> actualDtos = currencyService.getCurrencies();

        assertEquals(expectedDtos.size(), actualDtos.size());

        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i).code, actualDtos.get(i).code);
            assertEquals(expectedDtos.get(i).description, actualDtos.get(i).description);
        }
    }
}