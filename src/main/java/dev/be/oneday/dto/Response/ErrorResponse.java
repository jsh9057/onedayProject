package dev.be.oneday.dto.Response;

import dev.be.oneday.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private HttpStatus status;
    private String message;

    public ErrorResponse(ErrorType errorType){
        this.code = errorType.getCode();
        this.status = errorType.getHttpStatus();
        this.message = errorType.getMessage();
    }
}
