package ru.cryptopro.support.DssJavaClient.exception.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cryptopro.support.DssJavaClient.exception.SignException;
import ru.cryptopro.support.DssJavaClient.exception.handlers.helper.ErrorMessageHelper;

import java.util.Map;

@ControllerAdvice
public class SignExceptionHandler {
    @ExceptionHandler(SignException.class)
    public ResponseEntity<Map<String, Object>> exception(SignException exception) {
        return ResponseEntity.internalServerError().body(ErrorMessageHelper.getMessageBody(exception));
    }
}
