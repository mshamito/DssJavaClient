package ru.cryptopro.support.DssJavaClient.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cryptopro.support.DssJavaClient.entity.ConfirmationEntity;
import ru.cryptopro.support.DssJavaClient.service.ConfirmationService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/confirmations/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfirmationController {
    private final ConfirmationService confirmationService;

    public ConfirmationController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @GetMapping("/{username}")
    public List<ConfirmationEntity> confirmationList(@PathVariable String username) {
        return confirmationService.getConfirmationsForUser(username);
    }
}
