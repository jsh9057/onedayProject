package dev.be.oneday.service;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.HabitCheck;
import dev.be.oneday.domain.HabitJoin;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitCheckRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[비지니스 로직] - 습관 실천")
@ExtendWith(MockitoExtension.class)
class HabitCheckServiceTest {

    @InjectMocks
    private HabitCheckService habitCheckService;

    @Mock
    private HabitCheckRepository habitCheckRepository;
    @Mock
    private HabitJoinRepository habitJoinRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private HabitRepository habitRepository;

    @DisplayName("습관아이디와 유저정보를 주면, 실천여부를 저장한다.")
    @Test
    void givenHabitIdAndUserInfo_whenCreate_thenCreatesHabitChecked(){
        // given
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(1L)
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);
        Habit habit = Habit.builder()
                .habitId(2L)
                .build();
        HabitJoin habitJoin = HabitJoin.builder()
                .habitJoinId(3L)
                .userAccount(userAccount)
                .habit(habit)
                .build();
        given(userAccountRepository.findById(userAccountDto.getUserAccountId())).willReturn(Optional.of(userAccount));
        given(habitRepository.findById(habit.getHabitId())).willReturn(Optional.of(habit));
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habit.getHabitId(),userAccount.getUserAccountId()))
                .willReturn(Optional.of(habitJoin));
        given(habitCheckRepository.findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(any(Long.TYPE), any(Long.TYPE), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .willReturn(Page.empty());

        // when
        habitCheckService.create(habit.getHabitId(), userAccountDto);

        // then
        then(userAccountRepository).should().findById(userAccountDto.getUserAccountId());
        then(habitRepository).should().findById(habit.getHabitId());
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habit.getHabitId(), userAccount.getUserAccountId());
        then(habitCheckRepository).should().findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(any(Long.TYPE), any(Long.TYPE), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
        then(habitCheckRepository).should().save(any(HabitCheck.class));
    }

    @DisplayName("이미 실천한 습관을 중복실천하면, 예외를 던진다.")
    @Test
    void givenHabitIdAndUserInfo_whenAlreadyChecked_thenException(){
        // given
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(1L)
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);
        Habit habit = Habit.builder()
                .habitId(2L)
                .build();
        HabitJoin habitJoin = HabitJoin.builder()
                .habitJoinId(3L)
                .userAccount(userAccount)
                .habit(habit)
                .build();
        HabitCheck habitCheck = HabitCheck.builder()
                .habitCheckId(4L)
                .isYn(true)
                .build();
        PageImpl<HabitCheck> checked = new PageImpl<>(List.of(habitCheck));
        given(userAccountRepository.findById(userAccountDto.getUserAccountId())).willReturn(Optional.of(userAccount));
        given(habitRepository.findById(habit.getHabitId())).willReturn(Optional.of(habit));
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habit.getHabitId(),userAccount.getUserAccountId()))
                .willReturn(Optional.of(habitJoin));
        given(habitCheckRepository.findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(any(Long.TYPE), any(Long.TYPE), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .willReturn(checked);

        // when
        Throwable t = catchThrowable(() -> habitCheckService.create(habit.getHabitId(), userAccountDto));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.ALREADY_CHECKED_HABIT);
        then(userAccountRepository).should().findById(userAccountDto.getUserAccountId());
        then(habitRepository).should().findById(habit.getHabitId());
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habit.getHabitId(), userAccount.getUserAccountId());
        then(habitCheckRepository).should().findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(any(Long.TYPE), any(Long.TYPE), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @DisplayName("참여하지않은 습관의 실천여부를 저장하면, 예외를 던진다.")
    @Test
    void givenHabitIdAndUserInfo_whenNotJoinedHabit_thenException(){
        // given
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(1L)
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);
        Habit habit = Habit.builder()
                .habitId(2L)
                .build();
        given(userAccountRepository.findById(userAccountDto.getUserAccountId())).willReturn(Optional.of(userAccount));
        given(habitRepository.findById(habit.getHabitId())).willReturn(Optional.of(habit));
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habit.getHabitId(),userAccount.getUserAccountId()))
                .willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> habitCheckService.create(habit.getHabitId(), userAccountDto));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.NOT_JOINED_HABIT);
        then(userAccountRepository).should().findById(userAccountDto.getUserAccountId());
        then(habitRepository).should().findById(habit.getHabitId());
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habit.getHabitId(), userAccount.getUserAccountId());
    }

    @DisplayName("습관실천 아이디와 실천여부를 주면, 습관실천여부를 수정한다.")
    @Test
    void givenHabitCheckIdAndIsChecked_whenUpdate_thenUpdatesHabitCheck() {
        // given
        Long habitCheckId = 1L;
        boolean isCheck = false;
        HabitCheck habitCheck = HabitCheck.builder()
                .habitCheckId(habitCheckId)
                .isYn(true)
                .build();
        given(habitCheckRepository.findById(habitCheckId)).willReturn(Optional.of(habitCheck));

        // when
        habitCheckService.update(habitCheckId, isCheck);

        // then
        assertThat(habitCheck.getIsYn()).isFalse();
        then(habitCheckRepository).should().findById(habitCheckId);
        then(habitCheckRepository).should().save(habitCheck);
    }

    @DisplayName("습관실천 아이디를 주면, 습관실천여부를 삭제한다.")
    @Test
    void givenHabitCheckId_whenDelete_thenDeletesHabitCheck() {
        // Given
        Long habitCheckId = 1L;
        HabitCheck habitCheck = HabitCheck.builder()
                .habitCheckId(habitCheckId)
                .build();
        given(habitCheckRepository.findById(habitCheckId)).willReturn(Optional.of(habitCheck));

        // When
        habitCheckService.delete(habitCheckId);

        // Then
        assertThat(habitCheck.getIsDeleted()).isTrue();
        then(habitCheckRepository).should().findById(habitCheckId);
        then(habitCheckRepository).should().save(habitCheck);

    }

    @DisplayName("습관아이디와 유저아이디와 몇일 전까지 조회할지 정보를주면, 습관을 실천한 내역을 가져온다.")
    @Test
    void given_when_then() {
        // Given
        Long habitId = 1L;
        Long userAccountId = 2L;
        Long prevDate = 7L;
        HabitJoin habitJoin = HabitJoin.builder()
                .userAccount(UserAccount.builder()
                        .userAccountId(userAccountId)
                        .build())
                .habit(Habit.builder()
                        .habitId(habitId)
                        .build())
                .build();
        given(habitJoinRepository.findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountId)).willReturn(Optional.of(habitJoin));
        given(habitCheckRepository.findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(any(Long.TYPE),any(Long.TYPE),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class)))
                .willReturn(Page.empty());
        // When
        habitCheckService.getPrevHabitChecks(habitId, userAccountId, prevDate, Pageable.ofSize(7));

        // Then
        then(habitJoinRepository).should().findByHabit_HabitIdAndUserAccount_UserAccountId(habitId,userAccountId);
        then(habitCheckRepository).should().findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(any(Long.TYPE),any(Long.TYPE),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
    }
}