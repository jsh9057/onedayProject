package dev.be.oneday.dto.Response;

import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.HabitJoinDto;
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
public class HabitJoinResponse implements Serializable {
    Long habitId;
    String title;

    String memberNickname;
    LocalDateTime joinedAt;

    String creatorNickName;
    String creatorEmail;
    LocalDateTime createdAt;

    public static HabitJoinResponse from(HabitJoinDto habitJoinDto){
        if(habitJoinDto == null){
            throw new BaseException(ErrorType.VALUE_IS_NULL,"habitJoinDto is null");
        }
        return HabitJoinResponse.builder()
                .habitId(habitJoinDto.getHabitDto().getHabitId())
                .title(habitJoinDto.getHabitDto().getTitle())
                .memberNickname(habitJoinDto.getUserAccountDto().getNickname())
                .joinedAt(habitJoinDto.getCreatedAt())
                .creatorNickName(habitJoinDto.getHabitDto().getUserAccountDto().getNickname())
                .creatorEmail(habitJoinDto.getHabitDto().getUserAccountDto().getEmail())
                .createdAt(habitJoinDto.getHabitDto().getCreatedAt())
                .build();
    }

}
