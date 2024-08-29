package dev.be.oneday.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-001","입력받은 인자값이 잘못되었습니다."),

    HABIT_NOT_FOUND(HttpStatus.NOT_FOUND,"HABIT-001","습관을 찾을 수 없습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER-001","유저를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
