package com.tkachenya.currency.mapper;

import com.tkachenya.currency.domain.Currency;
import com.tkachenya.currency.domain.ExchangeRate;
import com.tkachenya.currency.dto.CurrencyDto;
import com.tkachenya.currency.dto.ExchangeRateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ObjectMapper {

    public abstract CurrencyDto currencyToCurrencyDto(Currency currency);

    @Mapping(target="code", source = "exchangeRate.quoteCurrency.code")
    public abstract ExchangeRateDto exchangeRateToExchangeRateDto(ExchangeRate exchangeRate);
}
