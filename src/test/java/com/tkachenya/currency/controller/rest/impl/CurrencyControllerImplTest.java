package com.tkachenya.currency.controller.rest.impl;

import com.tkachenya.currency.dto.CurrencyDto;
import com.tkachenya.currency.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerImplTest {
    @InjectMocks
    private CurrencyControllerImpl controller;

    @Mock
    private CurrencyService currencyService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetCurrenciesWhenNoCurrenciesFound() throws Exception {
        when(currencyService.getCurrencies()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/currencies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist()); // Проверяем, что тело ответа пустое
    }

    @Test
    void testGetCurrenciesWhenCurrenciesAreFound() throws Exception {
        var currencyDto1 = new CurrencyDto();
        currencyDto1.code = "USD";
        currencyDto1.description = "US Dollar";

        var currencyDto2 = new CurrencyDto();
        currencyDto2.code = "EUR";
        currencyDto2.description = "Euro";

        List<CurrencyDto> expectedCurrencies = List.of(currencyDto1, currencyDto2);

        when(currencyService.getCurrencies()).thenReturn(expectedCurrencies);

        mockMvc.perform(get("/api/v1//currencies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code", is("USD")))
                .andExpect(jsonPath("$[0].description", is("US Dollar")))
                .andExpect(jsonPath("$[1].code", is("EUR")))
                .andExpect(jsonPath("$[1].description", is("Euro")));
    }
}
