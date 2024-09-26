package dev.be.oneday.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-001","입력받은 인자값이 잘못되었습니다."),
    VALUE_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002","값이 비어있습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON-003", "예상치 못한 서버 에러입니다."),

    HABIT_NOT_FOUND(HttpStatus.NOT_FOUND,"HABIT-001","습관을 찾을 수 없습니다."),
    ALREADY_JOINED_HABIT(HttpStatus.BAD_REQUEST, "HABIT-002", "이미 참여한 습관입니다."),
    NOT_JOINED_HABIT(HttpStatus.BAD_REQUEST, "HABIT-003", "참여하지않은 습관입니다."),
    ALREADY_CHECKED_HABIT(HttpStatus.BAD_REQUEST,"HABIT-004", "이미 실천 또는 실천하지 못한 습관입니다."),
    HABIT_CHECK_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT-005","존재하지않는 습관 실천여부 입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER-001","유저를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
