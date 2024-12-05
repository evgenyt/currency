package com.tkachenya.currency.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Валюта")
public class CurrencyDto {
    @Schema(description = "Код")
    public String code;

    @Schema(description = "Описание")
    public String description;
}
