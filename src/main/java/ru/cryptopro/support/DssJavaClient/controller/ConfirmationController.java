package ru.cryptopro.support.DssJavaClient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cryptopro.support.DssJavaClient.entity.ConfirmationEntity;
import ru.cryptopro.support.DssJavaClient.service.ConfirmationService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/confirmations/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ConfirmationController {
    private final ConfirmationService confirmationService;


    @GetMapping("/{username}")
    public List<ConfirmationEntity> confirmationList(@PathVariable String username) {
        return confirmationService.getConfirmationsForUser(username);
    }
}
