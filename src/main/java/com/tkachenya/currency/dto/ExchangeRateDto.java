package com.tkachenya.currency.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Курс обмена валюты")
public class ExchangeRateDto {
    @Schema(description = "Дата")
    public LocalDate date;

    @Schema(description = "Код валюты")
    public String code;

    @Schema(description = "Обменный курс")
    public BigDecimal rate;
}
