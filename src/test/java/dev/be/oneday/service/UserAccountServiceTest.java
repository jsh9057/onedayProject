package dev.be.oneday.service;

import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[비지니스 로직] - 회원")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("유저 정보를 주면, 신규 유저를 생성하고 생성된 유저를 반환한다.")
    @Test
    void givenUser_whenCreate_thenCreatesUserAccount(){
        // given
        UserAccount userAccount = UserAccount.builder()
                .userId("userId")
                .password("pwd")
                .nickname("nick")
                .email("email@abc.com")
                .build();
        UserAccountDto userAccountDto = UserAccountDto.from(userAccount);

        given(userAccountRepository.save(ArgumentMatchers.any(UserAccount.class))).willReturn(userAccount);

        // when
        UserAccountDto actual = userAccountService.create(userAccountDto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("userId",userAccountDto.getUserId())
                .hasFieldOrPropertyWithValue("password",userAccountDto.getPassword())
                .hasFieldOrPropertyWithValue("nickname",userAccountDto.getNickname())
                .hasFieldOrPropertyWithValue("email",userAccountDto.getEmail())
                .hasFieldOrPropertyWithValue("createdBy",actual.getCreatedBy())
                .hasFieldOrProperty("createdAt");
        then(userAccountRepository).should().save(ArgumentMatchers.any(UserAccount.class));
    }
}