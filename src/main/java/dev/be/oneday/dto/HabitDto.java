package dev.be.oneday.dto;

import dev.be.oneday.domain.Habit;
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
public class HabitDto implements Serializable {
    private Long habitId;

    private UserAccountDto userAccountDto;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;

    public static HabitDto from(Habit habit){
        if(habit == null){ throw new BaseException(ErrorType.VALUE_IS_NULL,"habit is null"); }
        return HabitDto.builder()
                .habitId(habit.getHabitId())
                .userAccountDto(UserAccountDto.from(habit.getUserAccount()))
                .title(habit.getTitle())
                .content(habit.getContent())
                .createdAt(habit.getCreatedAt())
                .createdBy(habit.getCreatedBy())
                .modifiedAt(habit.getModifiedAt())
                .modifiedBy(habit.getModifiedBy())
                .isDeleted(habit.getIsDeleted())
                .build();
    }

    public Habit toEntity(UserAccount userAccount){
        if(userAccount == null) throw new BaseException(ErrorType.VALUE_IS_NULL,"userAccount is null");
        return Habit.builder()
                .userAccount(userAccount)
                .title(title)
                .content(content)
                .build();
    }
}
