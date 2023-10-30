package ru.cryptopro.support.DssJavaClient.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cryptopro.support.DssJavaClient.dto.confirmation.DssIDPCallbackDto;
import ru.cryptopro.support.DssJavaClient.dto.response.SignResponse;
import ru.cryptopro.support.DssJavaClient.service.CallbackService;

@Log4j2
@CrossOrigin
@RestController
@Hidden
public class DssCallbackController {
    private final CallbackService callbackService;

    public DssCallbackController(CallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @PostMapping("${app.config.idp_callback_endpoint}")
    public String catchIDPCallback(@RequestBody DssIDPCallbackDto callback) {
        callbackService.handleIDPCallback(callback);
       log.info("IDP callback received: {}", callback);
       return "";
    }

    @PostMapping("${app.config.ss_callback_endpoint}")
    public String catchSSCallback(@RequestBody SignResponse callback) {
        callbackService.handleSSCallback(callback);
        log.info("SS callback received: {}", callback);
        return "";
    }

}
