package dev.be.oneday.service;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@DisplayName("[비지니스 로직] - 습관")
@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @InjectMocks
    private HabitService habitService;

    @Mock
    private HabitRepository habitRepository;
    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("실천할 습관를 생성하면, 생성된 습관을 반환한다.")
    @Test
    void givenHabitInfo_whenCreate_thenCreatesHabit() {

        // given
        UserAccount user = UserAccount.builder()
                .userAccountId(1L)
                .build();
        Habit habit = Habit.builder()
                .habitId(1L)
                .userAccount(user)
                .title("testTitle")
                .content("testContents")
                .build();
        HabitDto habitDto = HabitDto.from(habit);
        given(userAccountRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        given(habitRepository.save(ArgumentMatchers.any(Habit.class))).willReturn(habit);

        // when
        HabitDto actual = habitService.create(habitDto);

        // then
        assertThat(actual).hasFieldOrPropertyWithValue("title", habit.getTitle());
        then(habitRepository).should().save(ArgumentMatchers.any(Habit.class));
    }

    @DisplayName("제목이나 내용이 공백일 경우, 예외를 던진다.")
    @Test
    void givenInvalidHabitInfo_whenCreate_thenException() {

        // given
        UserAccount user = UserAccount.builder()
                .userAccountId(1L)
                .build();
        Habit habit = Habit.builder()
                .habitId(1L)
                .userAccount(user)
                .title(" ")
                .content(" ")
                .build();
        HabitDto habitDto = HabitDto.from(habit);
        given(userAccountRepository.findById(1L)).willReturn(Optional.ofNullable(user));

        // when
        Throwable t = catchThrowable(() -> habitService.create(habitDto));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PARAMETER);
        then(userAccountRepository).should().findById(1L);
    }

    @DisplayName("모든 습관을 불러오면, 모든 습관을 반환한다.")
    @Test
    void givenPageable_whenGetAll_thenGetAllHabits() {

        // given
        Pageable pageable = Pageable.ofSize(20);
        given(habitRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(Page.empty());

        // when
        Page<HabitDto> habits = habitService.getAllHabits(pageable);

        // then
        assertThat(habits).isEmpty();
        then(habitRepository).should().findAll(pageable);
    }

    @DisplayName("id 로 습관을 조회하면, 해당 습관을 반환한다.")
    @Test
    void givenHabitId_whenSearch_thenReturnsHabit() {

        // given
        Long habitId = 1L;
        Habit habit = Habit.builder()
                .habitId(habitId)
                .userAccount(UserAccount.builder().build())
                .title("testTitle")
                .content("testContents")
                .build();

        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));

        // when
        HabitDto actual = habitService.getHabit(habitId);

        // then
        assertThat(actual).hasFieldOrPropertyWithValue("habitId", habit.getHabitId());
        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("존재하지않는 id 로 습관을 조회하면, 예외를 던진다.")
    @Test
    void givenNotExistHabitId_whenSearch_thenException() {

        // given
        Long habitId = 1L;

        given(habitRepository.findById(habitId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> habitService.getHabit(habitId));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.HABIT_NOT_FOUND);
        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("Id로 삭제를 요청하면, 습관을 삭제한다.")
    @Test
    void givenHabitId_whenDeletes_thenDeletesHabit() {

        // given
        Long habitId = 1L;
        Habit habit = Habit.builder()
                .habitId(habitId)
                .userAccount(UserAccount.builder().build())
                .title("testTitle")
                .content("testContents")
                .build();

        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));

        // when
        habitService.delete(habitId);

        // then
        assertThat(habit).hasFieldOrPropertyWithValue("isDeleted", true);
        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("존재하지않는 Id로 삭제를 요청하면, 예외를 던진다.")
    @Test
    void givenNotExistHabitId_whenDeletes_thenException() {

        // given
        Long habitId = 1L;

        given(habitRepository.findById(habitId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> habitService.delete(habitId));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.HABIT_NOT_FOUND);
        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("수정할 습관의 정보로 수정요청을 하면, 습관을 수정하고 수정된 습관을 반환한다.")
    @Test
    void givenHabitInfo_whenUpdate_thenUpdatesHabit() {

        // given
        Long habitId = 1L;
        Long userAccountId = 2L;
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userAccountId)
                .userId("test")
                .password("1234")
                .email("test@test.com")
                .nickname("testNick")
                .build();
        Habit habit = Habit.builder()
                .habitId(habitId)
                .userAccount(userAccount)
                .title("testTitle")
                .content("testContents")
                .build();
        HabitDto updateHabitDto = HabitDto.builder()
                .habitId(habitId)
                .userAccountDto(UserAccountDto.from(userAccount))
                .title("updateTitle")
                .content("updateContents")
                .build();

        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
        given(userAccountRepository.findById(userAccountId)).willReturn(Optional.of(userAccount));

        // when
        HabitDto actual = habitService.update(habitId, updateHabitDto);

        // then
        assertThat(actual).hasFieldOrPropertyWithValue("title", updateHabitDto.getTitle());
        assertThat(actual).hasFieldOrPropertyWithValue("content", updateHabitDto.getContent());
        assertThat(habit).hasFieldOrPropertyWithValue("content", updateHabitDto.getContent());
        assertThat(habit).hasFieldOrPropertyWithValue("content", updateHabitDto.getContent());

        then(habitRepository).should().findById(habitId);
        then(userAccountRepository).should().findById(userAccountId);
    }

    @DisplayName("존재하지않는 습관 id 로 수정 요청시, 예외를 던진다.")
    @Test
    void givenNotExistHabitId_whenUpdate_thenException() {

        // given
        Long habitId = 1L;
        HabitDto updateHabitDto = HabitDto.builder()
                .habitId(habitId)
                .title("updateTitle")
                .content("updateContent")
                .build();

        given(habitRepository.findById(habitId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> habitService.update(habitId, updateHabitDto));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType",ErrorType.HABIT_NOT_FOUND);

        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("존재하지않는 유저로 수정 요청시, 예외를 던진다.")
    @Test
    void givenNotExistUser_whenUpdate_thenException() {

        // given
        Long habitId = 1L;
        Long notExistUserAccountId = 3L;
        HabitDto updateHabitDto = HabitDto.builder()
                .habitId(habitId)
                .userAccountDto(UserAccountDto.builder().userAccountId(notExistUserAccountId).build())
                .title("updateTitle")
                .content("updateContents")
                .build();

        given(habitRepository.findById(habitId)).willReturn(Optional.of(Habit.builder().build()));
        given(userAccountRepository.findById(notExistUserAccountId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(()->habitService.update(habitId, updateHabitDto));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                        .hasFieldOrPropertyWithValue("errorType",ErrorType.USER_NOT_FOUND);

        then(habitRepository).should().findById(habitId);
        then(userAccountRepository).should().findById(notExistUserAccountId);
    }

    @DisplayName("제목이나 내용이 공백일때, 예외를 던진다.")
    @Test
    void givenInvalidHabitInfo_whenUpdate_thenException() {

        // given
        Long habitId = 1L;
        Long userAccountId = 1L;
        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userAccountId)
                .build();
        Habit habit = Habit.builder()
                .userAccount(userAccount)
                .title("title")
                .content("content")
                .build();
        HabitDto blinkHabit = HabitDto.builder()
                .userAccountDto(UserAccountDto.from(userAccount))
                .title(" ")
                .content("")
                .build();

        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
        given(userAccountRepository.findById(userAccountId)).willReturn(Optional.of(userAccount));

        // when
        Throwable t = catchThrowable(()->habitService.update(habitId, blinkHabit));

        // then
        assertThat(t)
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorType",ErrorType.INVALID_PARAMETER);

        then(habitRepository).should().findById(habitId);
        then(userAccountRepository).should().findById(userAccountId);
    }
}