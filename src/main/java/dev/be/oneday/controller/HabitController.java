package dev.be.oneday.controller;

import dev.be.oneday.domain.KeywordMongo;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.Request.HabitRequest;
import dev.be.oneday.dto.Response.HabitResponse;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.service.HabitService;
import dev.be.oneday.service.KeywordMongoService;
import dev.be.oneday.service.KeywordService;
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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/habits")
@RestController
public class HabitController {

    private final HabitService habitService;
    private final KeywordMongoService keywordMongoService;
    private final UserAccountService userAccountService;

    @GetMapping
    public ResponseEntity<Page<HabitResponse>> getAll(
            @RequestParam(required = false) String title,
            @PageableDefault(size = 20, sort="createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        Page<HabitResponse> habitResponses;
        if(title == null || title.isBlank()){
            habitResponses = habitService.getAllHabits(pageable).map(HabitResponse::from);
        }
        else{
//            habitResponses = keywordService.searchForKeyword(title,pageable).map(HabitResponse::from);
            habitResponses = keywordMongoService.searchForKeyword(title, pageable).map(HabitResponse::from);

        }
        return ResponseEntity.ok(habitResponses);
    }

    @GetMapping("{habitId}")
    public ResponseEntity<HabitResponse> get(@PathVariable Long habitId){
        HabitResponse habitResponse = HabitResponse.from(habitService.getHabit(habitId));
        return ResponseEntity.ok(habitResponse);
    }

//    @GetMapping
//    public ResponseEntity<Page<HabitResponse>> searchByTitle(
//            @RequestParam(required = true) String title,
//            @PageableDefault(size = 20, sort="createdAt", direction = Sort.Direction.DESC)Pageable pageable
//    ){
//        Page<HabitResponse> habitResponses = keywordService.searchForKeyword(title,pageable).map(HabitResponse::from);
//        return ResponseEntity.ok(habitResponses);
//
//    }


    @PostMapping
    public ResponseEntity<HabitResponse> create(
            @RequestParam(defaultValue = "1") Long userAccountId,
            @RequestBody HabitRequest habitRequest
    ){
        // TODO: 추후 삭제
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(userAccountId)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();
        HabitDto habitDto = habitService.create(habitRequest.toDto(tempUser));
        HabitResponse habitResponse = HabitResponse.from(habitDto);
//        keywordService.create(habitDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(habitResponse);
    }

    @PutMapping("{habitId}")
    public ResponseEntity<HabitResponse> update(
            @PathVariable Long habitId,
            @RequestParam(defaultValue = "1") Long userAccountId,
            @RequestBody HabitRequest habitRequest
    ){
        // TODO: 추후 삭제
        UserAccountDto tempUser = UserAccountDto.builder()
                .userAccountId(userAccountId)
                .userId("test")
                .password("1234")
                .nickname("Nickname")
                .email("test@email.com")
                .build();

        HabitResponse habitResponse = HabitResponse.from(habitService.update(habitId,habitRequest.toDto(tempUser)));

        return ResponseEntity.ok(habitResponse);
    }

    @DeleteMapping("{habitId}")
    public ResponseEntity<Void> delete(@PathVariable Long habitId){
        habitService.delete(habitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordMongo>> findAllKeyword(){
        return ResponseEntity.ok(keywordMongoService.findAllKeyword());
//        return ResponseEntity.ok(keywordService.findAllKeyword().stream().map(KeywordResponse::from).toList());
    }
}
