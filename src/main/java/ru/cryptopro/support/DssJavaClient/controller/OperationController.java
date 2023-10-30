package ru.cryptopro.support.DssJavaClient.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.operation.OperationDto;
import ru.cryptopro.support.DssJavaClient.service.OperationService;

import java.util.List;

@CrossOrigin
@Log4j2
@RestController
@RequestMapping(value = "/api/operations", produces = MediaType.APPLICATION_JSON_VALUE)
public class OperationController {
    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping(value = "/")
    public List<OperationDto> listAll(
            @RequestHeader String username, @RequestHeader(required = false) String password
    ) {
        log.info("Received request for user {}", username);
        return operationService.getAllActive(new UserCredentialDto(username, password));
    }

    @GetMapping(value = "/{id}")
    public OperationDto info(
            @RequestHeader String username, @RequestHeader(required = false) String password,
            @PathVariable String id
    ) {
        log.info("Received request for user {}", username);
        return operationService.getInfo(new UserCredentialDto(username, password), id);
    }
}
