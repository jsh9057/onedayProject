package dev.be.oneday.service;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.HabitJoin;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.HabitJoinDto;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitJoinRepository;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitJoinService {
    private final HabitJoinRepository habitJoinRepository;
    private final UserAccountRepository userAccountRepository;
    private final HabitRepository habitRepository;
    public void joinHabit(Long habitId, UserAccountDto userAccountDto){
        Long userAccountId = userAccountDto.getUserAccountId();
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND,"userAccountId:"+userAccountId));
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new BaseException(ErrorType.HABIT_NOT_FOUND,"habitId:"+habitId));

        if(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId, userAccountId).isPresent()){
            throw new BaseException(ErrorType.ALREADY_JOINED_HABIT);
        }
        else{
            HabitJoin habitJoin = HabitJoin.of(userAccount,habit);
            habitJoinRepository.save(habitJoin);
        }
    }


    public Page<HabitJoinDto> getHabitUsers(Long habitId, Pageable pageable){
        if(habitRepository.findById(habitId).isEmpty()){
            throw new BaseException(ErrorType.HABIT_NOT_FOUND,"habitId:"+habitId);
        }
        return habitJoinRepository.findByHabit_HabitId(habitId, pageable).map(HabitJoinDto::from);
    }

    public Page<HabitJoinDto> getUserHabits(UserAccountDto userAccountDto, Pageable pageable){
        if(userAccountRepository.findById(userAccountDto.getUserAccountId()).isEmpty()){
            throw new BaseException(ErrorType.USER_NOT_FOUND,"userAccountId:"+userAccountDto.getUserAccountId());
        }
        return habitJoinRepository.findByUserAccount_UserAccountId(userAccountDto.getUserAccountId(), pageable).map(HabitJoinDto::from);
    }
}
