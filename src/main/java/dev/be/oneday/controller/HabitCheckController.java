package dev.be.oneday.controller;

import dev.be.oneday.dto.HabitCheckDto;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.service.HabitCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class HabitCheckController {

    private final HabitCheckService habitCheckService;

    @PostMapping("/habits/{habitId}/checks")
    public ResponseEntity<Void> create(
            @PathVariable Long habitId,
            @RequestParam(defaultValue = "1") Long userAccountId
    ){
        // TODO: 추후 삭제
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(userAccountId)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();
        habitCheckService.create(habitId,tempUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/habits/{habitId}/checks/users/{userAccountId}")
    public ResponseEntity<List<HabitCheckDto>> getPrevHabitChecks(
            @PathVariable Long habitId,
            @PathVariable Long userAccountId,
            @RequestParam(required = false, defaultValue = "7") Long prevDate,
            @PageableDefault Pageable pageable
    ){
        List<HabitCheckDto> prevHabitChecks = habitCheckService.getPrevHabitChecks(habitId, userAccountId, prevDate, pageable);
        return ResponseEntity.ok(prevHabitChecks);
    }

    @GetMapping("/habits/{habitId}/checks/users")
    public ResponseEntity<List<List<HabitCheckDto>>> getPrevHabitChecksAllUsers(
            @PathVariable Long habitId,
            @RequestParam(required = false, defaultValue = "7") Long prevDate,
            @PageableDefault Pageable pageable
    ){
        List<List<HabitCheckDto>> prevHabitChecks = habitCheckService.getHabitChecksAllUsers(habitId, prevDate, pageable);
        return ResponseEntity.ok(prevHabitChecks);
    }

    @PutMapping("/checks/{habitCheckId}")
    public ResponseEntity<Void> update(
            @PathVariable Long habitCheckId,
            boolean isCheck
    ){
        habitCheckService.update(habitCheckId,isCheck);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/checks/{habitCheckId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long habitCheckId
    ){
        habitCheckService.delete(habitCheckId);
        return ResponseEntity.ok().build();
    }

}
