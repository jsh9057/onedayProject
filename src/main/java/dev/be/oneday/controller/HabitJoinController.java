package dev.be.oneday.controller;

import dev.be.oneday.dto.Response.HabitJoinResponse;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.service.HabitJoinService;
import dev.be.oneday.service.HabitService;
import dev.be.oneday.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class HabitJoinController {
    private final HabitJoinService habitJoinService;
    private final UserAccountService userAccountService;

    @PostMapping("/habits/{habitId}/habit-join")
    public ResponseEntity<Void> joinHabit(
            @PathVariable Long habitId
//            @RequestBody HabitJoinRequest habitJoinRequest

    ){
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(1L)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();
        habitJoinService.joinHabit(habitId,tempUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/habits/{habitId}/habit-join")
    public ResponseEntity<Page<HabitJoinResponse>> getHabitUsers(
            @PathVariable Long habitId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        Page<HabitJoinResponse> habitJoinResponses = habitJoinService.getHabitUsers(habitId, pageable).map(HabitJoinResponse::from);
        return ResponseEntity.ok(habitJoinResponses);
    }

    @GetMapping("/mypage/habit-join")
    public ResponseEntity<Page<HabitJoinResponse>> getMyHabits(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(1L)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();
        Page<HabitJoinResponse> habitJoinResponses = habitJoinService.getUserHabits(tempUser, pageable).map(HabitJoinResponse::from);
        return ResponseEntity.ok(habitJoinResponses);
    }
}
