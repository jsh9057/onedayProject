package dev.be.oneday.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-001","입력받은 인자값이 잘못되었습니다."),
    VALUE_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002","Value is null"),

    HABIT_NOT_FOUND(HttpStatus.NOT_FOUND,"HABIT-001","습관을 찾을 수 없습니다."),
    ALREADY_JOINED_HABIT(HttpStatus.BAD_REQUEST, "HABIT-002", "이미 가입된 습관입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER-001","유저를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
