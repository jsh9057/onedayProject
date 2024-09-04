package dev.be.oneday.dto;

import dev.be.oneday.domain.HabitJoin;
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
public class HabitJoinDto implements Serializable {
    private Long habitJoinId;

    private UserAccountDto userAccountDto;

    private Long habitId;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;

    public static HabitJoinDto from(HabitJoin habitJoin){
        if(habitJoin == null){ throw new BaseException(ErrorType.VALUE_IS_NULL,"habitJoin is null"); }
        return HabitJoinDto.builder()
                .habitJoinId(habitJoin.getHabitJoinId())
                .userAccountDto(UserAccountDto.from(habitJoin.getUserAccount()))
                .habitId(habitJoin.getHabit().getHabitId())
                .createdAt(habitJoin.getCreatedAt())
                .createdBy(habitJoin.getCreatedBy())
                .modifiedAt(habitJoin.getModifiedAt())
                .modifiedBy(habitJoin.getModifiedBy())
                .isDeleted(habitJoin.getIsDeleted())
                .build();
    }
}
