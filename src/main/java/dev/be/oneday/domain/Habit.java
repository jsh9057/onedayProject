package dev.be.oneday.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habit extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long habitId;

    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '로그인 아이디'")
    private String userId;
    @Column(nullable = false, columnDefinition = "varchar(255) COMMENT '로그인 비밀번호'")
    private String password;
    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '다른 사용자에게 노출될 이름'")
    private String nickName;
    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '이메일'")
    private String email;
}
