package com.tkachenya.currency.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
public class IndexController {
    private final Logger logger = getLogger(IndexController.class);

    @RequestMapping(method = GET)
    public ModelAndView getIndexPage() {
        logger.info("Requesting index page");

        return new ModelAndView("index.html");
    }
}
