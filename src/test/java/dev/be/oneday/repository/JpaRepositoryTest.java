package dev.be.oneday.repository;

import dev.be.oneday.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("[DB 연동]")
@DataJpaTest
public class JpaRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired UserAccountRepository userAccountRepository
    ){
        this.userAccountRepository = userAccountRepository;
    }


    @DisplayName("UserAccount 생성 테스트")
    @Test
    void test(){
        // given
        long preCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.builder().userId("userId")
                .password("pwd")
                .nickname("nick")
                .email("email@abc.com")
                .build();

        // when
        userAccountRepository.save(userAccount);

        // then
        assertThat(userAccountRepository.count())
                .isEqualTo(preCount+1);

    }
}
