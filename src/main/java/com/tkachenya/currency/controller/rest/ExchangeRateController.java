package com.tkachenya.currency.controller.rest;

import com.tkachenya.currency.dto.ExchangeRateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "Курсы валют")
public interface ExchangeRateController extends BaseController {

    @Operation(summary = "Получение списка курсов валют по ISO-коду валюты на текущий день и предыдущие дни")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExchangeRateDto.class)
                            )
                    )}),
            @ApiResponse(responseCode = "204", description = "Записи отсутствуют",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/exchange-rates/{code}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ExchangeRateDto>> getExchangeRates(
            @Parameter(name = "code", description = "ISO-код валюты")
            @PathVariable("code") String code,
            @Parameter(name = "daysCount", description = "Количество предыдущих дней, не включая текущий день")
            @RequestParam(value = "daysCount", required = false, defaultValue = "0") int daysCount);
}
