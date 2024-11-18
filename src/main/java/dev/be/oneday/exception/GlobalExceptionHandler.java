package dev.be.oneday.exception;

import dev.be.oneday.dto.Response.ErrorResponse;
import dev.be.oneday.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{
    private final MonitoringService monitoringService;

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<Object> baseException(BaseException ex) {
        ErrorType errorType = ex.getErrorType();
        ErrorResponse errorResponse = new ErrorResponse(errorType);
        log.error(String.format("[%s] %s - detail: %s", errorType.getCode(), errorType.getMessage(),ex.getDetails()));
        monitoringService.incrementErrorCount();
        return ResponseEntity.status(errorType.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> baseException(Exception ex) {
        log.error(String.format("[%s] %s - detail: %s", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.toString()));
        ErrorResponse errorResponse = new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR);
        monitoringService.incrementErrorCount();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
