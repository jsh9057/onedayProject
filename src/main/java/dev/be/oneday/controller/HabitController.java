package dev.be.oneday.controller;

import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.Request.HabitRequest;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.service.HabitService;
import dev.be.oneday.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/habits")
@RestController
public class HabitController {

    private final HabitService habitService;
    private final UserAccountService userAccountService;

    @GetMapping
    public ResponseEntity<Page<HabitDto>> getAll(
            @PageableDefault(size = 20, sort="createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        return ResponseEntity.ok(habitService.getAllHabits(pageable));
    }

    @GetMapping("{habitId}")
    public ResponseEntity<HabitDto> get(@PathVariable Long habitId){
        return ResponseEntity.ok(habitService.getHabit(habitId));
    }

    @PostMapping
    public ResponseEntity<HabitDto> create(
            @RequestBody HabitRequest habitRequest
    ){
        // TODO: 추후 삭제
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(1L)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.create(habitRequest.toDto(tempUser)));
    }

    @PutMapping("{habitId}")
    public ResponseEntity<HabitDto> update(
            @PathVariable Long habitId,
            @RequestBody HabitRequest habitRequest
    ){
        // TODO: 추후 삭제
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(1L)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();
        return ResponseEntity.ok(habitService.update(habitId,habitRequest.toDto(tempUser)));
    }

    @DeleteMapping("{habitId}")
    public ResponseEntity<Void> delete(@PathVariable Long habitId){
        habitService.delete(habitId);
        return ResponseEntity.ok().build();
    }
}
