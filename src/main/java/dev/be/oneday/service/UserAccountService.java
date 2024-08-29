package dev.be.oneday.service;

import dev.be.oneday.domain.UserAccount;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountDto create(UserAccountDto userAccountDto){
        UserAccount saved = userAccountRepository.save(userAccountDto.toEntity());
        return UserAccountDto.fromEntity(saved);
    }

    public UserAccountDto getUserAccount(Long userAccountId){
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
                .orElseThrow(()-> new BaseException(ErrorType.USER_NOT_FOUND,"userAccountId:"+userAccountId));
        return UserAccountDto.fromEntity(userAccount);
    }
}
