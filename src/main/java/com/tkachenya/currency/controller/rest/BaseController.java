package com.tkachenya.currency.controller.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.web.bind.annotation.RequestMapping;

@OpenAPIDefinition(
        info = @Info(
                title = "API для работы с курсами валют",
                version = "1.0"
        )
)
@RequestMapping(value = "/api/v1")
public interface BaseController {
}
