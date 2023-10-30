package ru.cryptopro.support.DssJavaClient.exception.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cryptopro.support.DssJavaClient.exception.DocumentException;
import ru.cryptopro.support.DssJavaClient.exception.handlers.helper.ErrorMessageHelper;

import java.util.Map;

@ControllerAdvice
public class DocumentExceptionHandler {
    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<Map<String, Object>> exception(DocumentException exception) {
        return ResponseEntity.badRequest().body(ErrorMessageHelper.getMessageBody(exception));
    }
}
