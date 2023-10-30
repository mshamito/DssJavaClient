package ru.cryptopro.support.DssJavaClient.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SignException extends RuntimeException{
    public SignException(String message) {
        super(message);
        log.error(message);
    }
}
