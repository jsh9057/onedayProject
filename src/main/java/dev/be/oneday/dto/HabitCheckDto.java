package dev.be.oneday.dto;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.HabitCheck;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitCheckDto implements Serializable {
    private Long habitCheckId;

    private UserAccountDto userAccountDto;

    private HabitDto habitDto;

    private Boolean isYn;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;

    public HabitCheck toEntity(UserAccount userAccount, Habit habit){
        if(userAccount == null) throw new BaseException(ErrorType.VALUE_IS_NULL,"userAccount is null");
        if(habit == null) throw new BaseException(ErrorType.VALUE_IS_NULL,"habit is null");
        return HabitCheck.builder()
                .userAccount(userAccount)
                .habit(habit)
                .isYn(isYn)
                .build();
    }

    public static HabitCheckDto from(HabitCheck habitCheck){
        if(habitCheck==null){
            throw new BaseException(ErrorType.VALUE_IS_NULL, "habitCheck is null");
        }
        return HabitCheckDto.builder()
                .habitCheckId(habitCheck.getHabitCheckId())
                .userAccountDto(UserAccountDto.from(habitCheck.getUserAccount()))
                .habitDto(HabitDto.from(habitCheck.getHabit()))
                .isYn(habitCheck.getIsYn())
                .createdAt(habitCheck.getCreatedAt())
                .createdBy(habitCheck.getCreatedBy())
                .modifiedAt(habitCheck.getModifiedAt())
                .modifiedBy(habitCheck.getModifiedBy())
                .isDeleted(habitCheck.getIsDeleted())
                .build();
    }
}
