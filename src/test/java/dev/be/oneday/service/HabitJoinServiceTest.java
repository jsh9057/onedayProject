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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[비지니스 로직] - 습관 참여")
@ExtendWith(MockitoExtension.class)
class HabitJoinServiceTest {

    @InjectMocks
    private HabitJoinService habitJoinService;

    @Mock
    private HabitJoinRepository habitJoinRepository;
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("습관 아이디와 유저아이디를 입력하면, 습관에 참여한다.")
    @Test
    void givenHabitIdAndUserAccountId_whenCreate_thenCreatesHabitJoin(){
        // given
        Long userAccountId = 1L;
        Long habitId = 2L;
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userAccountId)
                .build();
        Habit habit = Habit.builder()
                .habitId(habitId)
                .userAccount(UserAccount.builder()
                        .userAccountId(2L)
                        .build())
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);
        HabitJoin habitJoin = HabitJoin.of(userAccount,habit);
        given(userAccountRepository.findById(userAccountId)).willReturn(Optional.of(userAccount));
        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountId)).willReturn(Optional.empty());
        given(habitJoinRepository.save(any(HabitJoin.class))).willReturn(habitJoin);

        // when
        habitJoinService.create(habitId,userAccountDto);

        // then
        then(userAccountRepository).should().findById(userAccountId);
        then(habitRepository).should().findById(habitId);
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountId);
        then(habitJoinRepository).should().save(any(HabitJoin.class));
    }

    @DisplayName("이미 해당 습관에 참여했다면, 예외를 던진다.")
    @Test
    void givenAlreadyJoinedHabitIdAndUserAccountId_whenCreate_thenException(){
        // given
        Long userAccountId = 1L;
        Long habitId = 2L;
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userAccountId)
                .build();
        Habit habit = Habit.builder()
                .userAccount(UserAccount.builder()
                        .userAccountId(2L)
                        .build())
                .habitId(habitId)
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);
        HabitJoin habitJoin = HabitJoin.of(userAccount,habit);
        given(userAccountRepository.findById(userAccountId)).willReturn(Optional.of(userAccount));
        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountId)).willReturn(Optional.of(habitJoin));

        // when
        Throwable t = catchThrowable(() -> habitJoinService.create(habitId, userAccountDto));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.ALREADY_JOINED_HABIT);
        then(userAccountRepository).should().findById(userAccountId);
        then(habitRepository).should().findById(habitId);
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountId);
    }

    @DisplayName("유저 아이디로, 유저가 가입한 습관들을 가져온다.")
    @Test
    void givenUserAccountId_whenSearch_thenUserJoinedHabits(){
        // given
        Long userAccountId = 1L;
        Pageable pageable = Pageable.ofSize(20);
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userAccountId)
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);
        given(userAccountRepository.findById(userAccountId)).willReturn(Optional.of(userAccount));
        given(habitJoinRepository.findByUserAccount_UserAccountId(userAccountId,pageable)).willReturn(Page.empty());

        // when
        Page<HabitJoinDto> userHabits = habitJoinService.getUserHabits(userAccountDto, pageable);

        // then
        assertThat(userHabits).isEqualTo(Page.empty());
        then(userAccountRepository).should().findById(userAccountId);
        then(habitJoinRepository).should().findByUserAccount_UserAccountId(userAccountId,pageable);
    }

    @DisplayName("습관 아이디로, 습관에 가입한 유저들을 가져온다.")
    @Test
    void givenHabitId_whenSearch_thenUserJoinedHabits(){
        // given
        Long userAccountId = 1L;
        Long habitId = 1L;
        Pageable pageable = Pageable.ofSize(10);

        given(habitRepository.findById(habitId)).willReturn(Optional.of(Habit.builder().build()));
        given(habitJoinRepository.findByHabit_HabitId(habitId,pageable)).willReturn(Page.empty());

        // when
        Page<HabitJoinDto> userHabits = habitJoinService.getHabitUsers(habitId, pageable);

        // then
        assertThat(userHabits).isEqualTo(Page.empty());
        then(habitRepository).should().findById(habitId);
        then(habitJoinRepository).should().findByHabit_HabitId(habitId,pageable);
    }

    @DisplayName("습관아이디와 유저정보로, 습관 참여여부를 삭제한다.")
    @Test
    void givenHabitIdAndUserInfo_whenDelete_thenDeletesHabitJoined(){
        // given
        UserAccountDto userAccountDto = UserAccountDto.builder()
                .userAccountId(1L)
                .build();
        Long habitId = 2L;
        HabitJoin habitJoin = HabitJoin.builder()
                .habitJoinId(3L)
                .build();
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountDto.getUserAccountId())).willReturn(Optional.of(habitJoin));

        // when
        habitJoinService.delete(habitId,userAccountDto);

        // then
        assertThat(habitJoin.getIsDeleted()).isTrue();
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountDto.getUserAccountId());
        then(habitJoinRepository).should().save(habitJoin);
    }
}