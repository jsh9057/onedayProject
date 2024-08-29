package dev.be.oneday.dto;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.UserAccount;
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

    public static HabitDto fromEntity(Habit habit){
        return HabitDto.builder()
                .habitId(habit.getHabitId())
                .userAccountDto(UserAccountDto.fromEntity(habit.getUserAccount()))
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
        if(userAccount == null) throw new NullPointerException("유저가 null 입니다.");
        return Habit.builder()
                .userAccount(userAccount)
                .title(title)
                .content(content)
                .build();
    }
}
