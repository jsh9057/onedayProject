package dev.be.oneday.dto;

import dev.be.oneday.domain.UserAccount;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto implements Serializable {

    private Long userAccountId;

    private String userId;

    private String password;

    private String nickname;

    private String email;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;

    public static UserAccountDto fromEntity(UserAccount userAccount) {
        return UserAccountDto.builder()
                .userAccountId(userAccount.getUserAccountId())
                .userId(userAccount.getUserId())
                .password(userAccount.getPassword())
                .nickname(userAccount.getNickname())
                .email(userAccount.getEmail())
                .createdAt(userAccount.getCreatedAt())
                .createdBy(userAccount.getCreatedBy())
                .modifiedAt(userAccount.getModifiedAt())
                .modifiedBy(userAccount.getModifiedBy())
                .isDeleted(userAccount.getIsDeleted())
                .build();
    }

    public UserAccount toEntity(){
        return UserAccount.builder()
                .userAccountId(userAccountId)
                .userId(userId)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
