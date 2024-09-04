package dev.be.oneday.dto.Response;

import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class HabitResponse implements Serializable {
    Long habitId;
    String nickname;
    String email;
    String title;
    String content;
    LocalDateTime createdAt;

    static public HabitResponse from (HabitDto habitDto){
        if(habitDto == null){
            throw new BaseException(ErrorType.VALUE_IS_NULL,"habitDto is null");
        }
        if(habitDto.getUserAccountDto()==null){
            throw new BaseException(ErrorType.VALUE_IS_NULL,"userAccountDto is null");
        }
        return HabitResponse.builder()
                .habitId(habitDto.getHabitId())
                .nickname(habitDto.getUserAccountDto().getNickname())
                .email(habitDto.getUserAccountDto().getEmail())
                .title(habitDto.getTitle())
                .content(habitDto.getContent())
                .createdAt(habitDto.getCreatedAt())
                .build();
    }
}
