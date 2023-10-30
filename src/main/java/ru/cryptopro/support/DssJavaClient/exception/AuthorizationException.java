package ru.cryptopro.support.DssJavaClient.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
        log.error(message);
    }
}
