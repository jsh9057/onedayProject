package dev.be.oneday.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitJoinDto {
    private Long habitJoinId;

    private UserAccountDto userAccountDto;

    private HabitDto habitDto;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;
}
