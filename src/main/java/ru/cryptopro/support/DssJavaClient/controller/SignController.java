package ru.cryptopro.support.DssJavaClient.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.response.SignResponse;
import ru.cryptopro.support.DssJavaClient.dto.sign.SignDocsListDto;
import ru.cryptopro.support.DssJavaClient.service.SignService;

@CrossOrigin
@Log4j2
@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class SignController {
    private final SignService signService;

    public SignController(
            SignService signService
    ) {
        this.signService = signService;
    }

    @Operation(summary = "Sign documents pack")
    @PostMapping(value = "/sign")
    public SignResponse sign(
            @RequestHeader String username, @RequestHeader(required = false) String password,
            @RequestBody SignDocsListDto signDocsList
    ) {
        log.info("Received request for user {}", username);
        return signService.sign(new UserCredentialDto(username, password), signDocsList);
    }
}
