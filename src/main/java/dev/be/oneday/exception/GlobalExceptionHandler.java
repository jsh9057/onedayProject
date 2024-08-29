package dev.be.oneday.exception;

import dev.be.oneday.dto.Response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(BaseException ex) {
        ErrorType errorType = ex.getErrorType();
        ErrorResponse errorResponse = new ErrorResponse(errorType);
        log.error(String.format("[%s] %s - detail: %s", errorType.getCode(), errorType.getMessage(),ex.getDetails()));
        return ResponseEntity.status(errorType.getHttpStatus()).body(errorResponse);
    }
}