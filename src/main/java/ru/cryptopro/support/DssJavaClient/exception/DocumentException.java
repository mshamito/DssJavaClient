package ru.cryptopro.support.DssJavaClient.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DocumentException extends RuntimeException {
    public DocumentException(String message) {
        super(message);
        log.error(message);
    }
}