package dev.be.oneday.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class BaseException extends RuntimeException {
    private ErrorType errorType;
    private String details;

    public BaseException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String details) {
        this.errorType = errorType;
        this.details = details;
    }
}
