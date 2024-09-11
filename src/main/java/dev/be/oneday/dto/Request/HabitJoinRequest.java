package dev.be.oneday.dto.Request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class HabitJoinRequest {
    Long habitId;
}
