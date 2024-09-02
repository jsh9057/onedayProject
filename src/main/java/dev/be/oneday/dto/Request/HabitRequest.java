package dev.be.oneday.dto.Request;

import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.UserAccountDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class HabitRequest {
    String title;
    String content;

    public HabitDto toDto(UserAccountDto userAccountDto) {
        return HabitDto.builder()
                .userAccountDto(userAccountDto)
                .title(title)
                .content(content)
                .build();
    }
}
