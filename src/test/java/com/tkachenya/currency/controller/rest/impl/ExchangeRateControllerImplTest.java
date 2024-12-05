package com.tkachenya.currency.controller.rest.impl;

import com.tkachenya.currency.dto.ExchangeRateDto;
import com.tkachenya.currency.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExchangeRateControllerImplTest {
    @InjectMocks
    private ExchangeRateControllerImpl controller;

    @Mock
    private ExchangeRateService exchangeRateService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetExchangeRatesWhenNoRatesFound() throws Exception {
        var baseCode = "USD";
        int daysCount = 7;
        var startDate = LocalDate.parse("2022-11-10");
        var endDate = startDate.minusDays(daysCount);

        when(exchangeRateService.getExchangeRates(baseCode, startDate, endDate))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/exchange-rates/{baseCode}?daysCount={daysCount}", baseCode, daysCount)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetExchangeRatesWhenRatesAreFound() throws Exception {
        var baseCode = "USD";
        int daysCount = 7;
        var startDate = LocalDate.parse("2024-12-05");
        var endDate = startDate.minusDays(daysCount);

        var rate1 = new ExchangeRateDto();
        rate1.date = startDate;
        rate1.code = "EUR";
        rate1.rate = new BigDecimal("1.12");

        var rate2 = new ExchangeRateDto();
        rate2.date = startDate;
        rate2.code = "GBP";
        rate2.rate = new BigDecimal("0.89");

        List<ExchangeRateDto> rates = List.of(rate1, rate2);

        when(exchangeRateService.getExchangeRates(baseCode, startDate, endDate)).thenReturn(rates);

        mockMvc.perform(get("/api/v1/exchange-rates/{baseCode}?daysCount={daysCount}", baseCode, daysCount)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2024-12-05")))
                .andExpect(jsonPath("$[0].code", is("EUR")))
                .andExpect(jsonPath("$[0].rate", is(1.12)))
                .andExpect(jsonPath("$[1].date", is("2024-12-05")))
                .andExpect(jsonPath("$[1].code", is("GBP")))
                .andExpect(jsonPath("$[1].rate", is(0.89)));
    }
}
