package dev.be.oneday.domain;

import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
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
public class HabitCheck extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long habitCheckId;

    @ManyToOne(optional = false)@JoinColumn(name = "userAccountId", columnDefinition = "bigint COMMENT '회원 id'")
    private UserAccount userAccount;

    @ManyToOne(optional = false)@JoinColumn(name = "habitId", columnDefinition = "bigint COMMENT '습관 id'")
    private Habit habit;

    @Column(columnDefinition = "varchar(1) default 'Y' NOT NULL COMMENT '습관 실천여부'")
    @Builder.Default
//    @Column(nullable = false)@ColumnDefault("'Y'")
    private Boolean isYn=true;

    public static HabitCheck of(UserAccount userAccount, Habit habit, Boolean isYn){
        if(userAccount == null) throw new BaseException(ErrorType.USER_NOT_FOUND,"userAccount is null");
        if(habit == null) throw new BaseException(ErrorType.HABIT_NOT_FOUND,"habit is null");
        return HabitCheck.builder()
                .userAccount(userAccount)
                .habit(habit)
                .isYn(isYn)
                .build();
    }
}
