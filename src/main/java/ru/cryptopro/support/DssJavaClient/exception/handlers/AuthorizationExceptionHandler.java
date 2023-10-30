package ru.cryptopro.support.DssJavaClient.exception.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cryptopro.support.DssJavaClient.exception.AuthorizationException;
import ru.cryptopro.support.DssJavaClient.exception.handlers.helper.ErrorMessageHelper;

import java.util.Map;

@ControllerAdvice
public class AuthorizationExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Map<String, Object>> exception(AuthorizationException exception) {
        return ResponseEntity.badRequest().body(ErrorMessageHelper.getMessageBody(exception));
    }
}
