package dev.be.oneday.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitCheckDto {
    private Long habitCheckId;

    private UserAccountDto userAccountDto;

    private HabitDto habitDto;

    private Boolean isYn;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;

}
