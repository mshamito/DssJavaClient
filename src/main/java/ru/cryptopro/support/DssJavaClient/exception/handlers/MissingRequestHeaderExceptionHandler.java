package ru.cryptopro.support.DssJavaClient.exception.handlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cryptopro.support.DssJavaClient.exception.handlers.helper.ErrorMessageHelper;

import java.util.Map;

@Log4j2
@ControllerAdvice
public class MissingRequestHeaderExceptionHandler {
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> exception(MissingRequestHeaderException exception) {
        log.error(exception);
        return ResponseEntity.badRequest().body(ErrorMessageHelper.getMessageBody(exception));
    }
}
