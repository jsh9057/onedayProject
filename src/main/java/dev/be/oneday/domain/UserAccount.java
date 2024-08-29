package dev.be.oneday.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@SQLRestriction("is_deleted = 'N'")
public class UserAccount extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long userAccountId;

    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '로그인 아이디'")
    private String userId;
    @Column(nullable = false, columnDefinition = "varchar(255) COMMENT '로그인 비밀번호'")
    private String password;
    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '다른 사용자에게 노출될 이름'")
    private String nickname;
    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '이메일'")
    private String email;
}
