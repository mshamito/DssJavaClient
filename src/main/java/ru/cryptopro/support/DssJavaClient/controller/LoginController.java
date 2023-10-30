package ru.cryptopro.support.DssJavaClient.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.service.AuthService;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Get access token for user")
    @GetMapping(value = "/login")
    public AuthResponse login(@RequestHeader String username, @RequestHeader(required = false) String password) {
        log.info("Received request for user {}", username);
        return authService.login(new UserCredentialDto(username, password));
    }

    @Operation(summary = "Get access token for operator")
    @GetMapping(value = "/login/operator")
    public AuthResponse login() {
        log.info("Received request for operator");
        return authService.login(new UserCredentialDto(true));
    }

    @Operation(summary = "Get delegated access token for user")
    @GetMapping(value = "/login/delegated")
    public AuthResponse delegatedLogin(@RequestHeader String username, @RequestHeader(required = false) String password) {
        log.info("Received delegated auth request for user {}", username);
        UserCredentialDto userCredential = new UserCredentialDto(username, password);
        userCredential.setUsingOperatorCertForAuth(true);
        return authService.login(userCredential);
    }
}
