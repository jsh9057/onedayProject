package dev.be.oneday.service;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.HabitCheck;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.HabitCheckDto;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitCheckRepository;
import dev.be.oneday.repository.HabitJoinRepository;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class HabitCheckService {

    private final HabitCheckRepository habitCheckRepository;
    private final HabitRepository habitRepository;
    private final UserAccountRepository userAccountRepository;
    private final HabitJoinRepository habitJoinRepository;

    public void create(Long habitId, UserAccountDto userAccountDto){
        UserAccount userAccount = userAccountRepository.findById(userAccountDto.getUserAccountId())
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND, "userAccountId:" + userAccountDto.getUserAccountId()));
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new BaseException(ErrorType.HABIT_NOT_FOUND,"habitId:"+habitId));

        boolean isJoined = habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountDto.getUserAccountId()).isPresent();
        if( !isJoined ){
            throw new BaseException(ErrorType.NOT_JOINED_HABIT,String.format("habitId:{%s} userAccountId:{%s}",habitId,userAccountDto.getUserAccountId()));
        }
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.now();
        Pageable pageable = Pageable.ofSize(1);
        boolean isTodayChecked = habitCheckRepository.findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(userAccount.getUserAccountId(), habit.getHabitId(), startDatetime, endDatetime, pageable)
                .stream()
                .anyMatch(HabitCheck::getIsYn);
        if( isTodayChecked ){
            throw new BaseException(ErrorType.ALREADY_CHECKED_HABIT,"already checked true");
        }
        HabitCheck habitCheck = HabitCheck.of(userAccount,habit,true);
        habitCheckRepository.save(habitCheck);
    }

    public void update(Long habitCheckId, boolean isCheck){
        HabitCheck habitCheck = habitCheckRepository.findById(habitCheckId)
                .orElseThrow(() -> new BaseException(ErrorType.HABIT_CHECK_NOT_FOUND, "habitCheckId:" + habitCheckId));
        habitCheck.setIsYn(isCheck);
        habitCheckRepository.save(habitCheck);
    }

    @Transactional(readOnly = true)
    public List<HabitCheckDto> getPrevHabitChecks(Long habitId, Long userAccountId, Long prevDate, Pageable pageable){
        habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId, userAccountId)
                .orElseThrow(() -> new BaseException(ErrorType.NOT_JOINED_HABIT));
        LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(prevDate-1),LocalTime.of(0,0,0) );
        LocalDateTime end = LocalDateTime.now();
        return habitCheckRepository.findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(userAccountId,habitId,start,end,pageable)
                .map(HabitCheckDto::from)
                .stream().toList();
    }

    public List<List<HabitCheckDto>> getHabitChecksAllUsers(Long habitId, Long prevDate, Pageable pageable){
        return habitJoinRepository.findByHabit_HabitId(habitId, Pageable.ofSize(10))
                .stream()
                .map(habitJoin -> {
                        Long userAccountId = habitJoin.getUserAccount().getUserAccountId();
                        return getPrevHabitChecks(habitId,userAccountId,prevDate,pageable);
                })
                .toList();
    }

    public void delete(Long habitCheckId){
        HabitCheck habitCheck = habitCheckRepository.findById(habitCheckId)
                .orElseThrow(() -> new BaseException(ErrorType.HABIT_CHECK_NOT_FOUND, "habitCheckId:"+habitCheckId));
        habitCheck.setIsDeleted(true);
        habitCheckRepository.save(habitCheck);
    }
}

