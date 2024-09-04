package dev.be.oneday.service;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final UserAccountRepository userAccountRepository;

    public HabitDto create(HabitDto habitDto){
        UserAccount userAccount = userAccountRepository.findById(habitDto.getUserAccountDto().getUserAccountId())
                .orElseThrow(()-> new BaseException(ErrorType.USER_NOT_FOUND,"userAccountId:"+habitDto.getUserAccountDto().getUserAccountId()));

        if(habitDto.getTitle().isBlank()){ throw new BaseException(ErrorType.INVALID_PARAMETER,"title is empty"); }
        if(habitDto.getContent().isBlank()){ throw new BaseException(ErrorType.INVALID_PARAMETER,"content is empty"); }

        Habit saved = habitRepository.save(habitDto.toEntity(userAccount));
        return HabitDto.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<HabitDto> getAllHabits(Pageable pageable){
        return habitRepository.findAll(pageable).map(HabitDto::from);
    }

    @Transactional(readOnly = true)
    public HabitDto getHabit(Long habitId){
        return habitRepository.findById(habitId)
                .map(HabitDto::from)
                .orElseThrow(()->new BaseException(ErrorType.HABIT_NOT_FOUND,"habitId:"+habitId));
    }

    public void delete(Long habitId){
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(()->new BaseException(ErrorType.HABIT_NOT_FOUND,"habitId:"+habitId));
        habit.setIsDeleted(true);
        habitRepository.save(habit);
    }

    public HabitDto update(Long habitId, HabitDto habitDto){
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(()->new BaseException(ErrorType.HABIT_NOT_FOUND,"habitId:"+habitId));

        UserAccount userAccount = userAccountRepository.findById(habitDto.getUserAccountDto().getUserAccountId())
                .orElseThrow(()-> new BaseException(ErrorType.USER_NOT_FOUND,"userAccountId:"+habitDto.getUserAccountDto().getUserAccountId()));

        if(habit.getUserAccount().equals(userAccount)){
            if(habitDto.getTitle().isBlank()){ throw new BaseException(ErrorType.INVALID_PARAMETER,"title is empty"); }
            if(habitDto.getContent().isBlank()){ throw new BaseException(ErrorType.INVALID_PARAMETER,"content is empty"); }

            habit.setTitle(habitDto.getTitle());
            habit.setContent(habitDto.getContent());
            habitRepository.save(habit);
            return HabitDto.from(habit);
        }
        // TODO: 수정자와 작성자가 다를 경우 수정없이 원본을 넘기지만, 정상적 처리인지 아닌지 response 를 만들어 구분하는게 좋아보임.
        return HabitDto.from(habit);
    }
}