package dev.be.oneday.service;

import dev.be.oneday.domain.Habit;
import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.repository.KeywordRepository;
import dev.be.oneday.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@ExtendWith({MockitoExtension.class})
public class HabitServiceTransactionTest {

    @Autowired
    private HabitService habitService;

//    @MockBean
//    private KeywordService keywordService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @MockBean
    private KeywordRepository keywordRepository;


    @BeforeEach
    void cleanUp(){
        log.warn("----------------------------------------------------start BeforeEach----------------------------------------------------");
        habitRepository.deleteAll();
        userAccountRepository.deleteAll();
        keywordRepository.deleteAll();
        keywordRepository.deleteAll();
        log.warn("----------------------------------------------------end BeforeEach----------------------------------------------------");
    }

    @DisplayName("습관생성시 키워드생성은 실패했을경우, 습관만 생성한다.")
    @Test
    void givenHabitAndKeywordInfo_whenCreate_thenCreatesHabitAndFailCreatesKeyword() throws Exception {
        UserAccount user = saveUserAccount();

        // given
        Habit habit = Habit.builder()
                .userAccount(user)
                .title("매일 영어공부 하기")
                .content("밥먹듯 영어를 공부합시다")
                .build();
        HabitDto habitDto = HabitDto.from(habit);
        when(keywordRepository.saveAll(any())).thenThrow(new BaseException(ErrorType.INTERNAL_SERVER_ERROR));
//        doThrow(new BaseException(ErrorType.INTERNAL_SERVER_ERROR)).when(keywordService).create(any(HabitDto.class));

        // when
//        Throwable throwable = catchThrowable(() -> habitService.create(habitDto));
        habitService.create(habitDto);

        // then
//        assertThat(throwable).isInstanceOf(BaseException.class);
        then(keywordRepository).should().saveAll(any());
        assertThat(habitRepository.count()).isEqualTo(1);
        assertThat(keywordRepository.count()).isEqualTo(0);
    }

    private UserAccount saveUserAccount(){
        UserAccount user = UserAccount.builder()
                .userId("testUser")
                .nickname("testNickname")
                .email("test1@test.com")
                .password("testPassword")
                .build();
        return userAccountRepository.save(user);
    }
}
